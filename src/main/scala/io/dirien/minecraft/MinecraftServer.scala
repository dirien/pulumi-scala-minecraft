package io.dirien.minecraft

import com.pulumi.core.Output
import com.pulumi.digitalocean.{Droplet, DropletArgs, SshKey, SshKeyArgs}
import com.pulumi.{Context, Pulumi}
import com.pulumi.command.remote.{Command, CommandArgs}
import com.pulumi.command.remote.inputs.ConnectionArgs

import scala.io.Source

class MinecraftServer(val image: String, val region: String, val size: String) {

  def readFile(path: String): String = {
    val file = Source.fromFile(path)
    val fileContent = try file.mkString finally file.close()
    fileContent
  }

  def infrastructure(): Output[String] = {
    val cloudInit = readFile("src/main/resources/cloud-init.yaml")
    val sshPublicKeyString = readFile("src/main/resources/minecraft.pub")

    val sshKey = new SshKey("minecraft-scala", SshKeyArgs.builder
      .publicKey(sshPublicKeyString)
      .build())

    val fingerprints: Output[java.util.List[String]] = sshKey.fingerprint().apply(fingerprint => {
      Output.listBuilder().add(fingerprint).build()
    })

    val minecraftServer = new Droplet("minecraft", DropletArgs.builder
      .image(this.image)
      .region(this.region)
      .size(this.size)
      .userData(cloudInit)
      .sshKeys(
        fingerprints
      )
      .build())

    val privateKey = readFile("src/main/resources/minecraft")

    new Command("install-minecraft", CommandArgs.builder()
      .connection(ConnectionArgs.builder()
        .host(minecraftServer.ipv4Address())
        .privateKey(privateKey)
        .user("root")
        .build())
      .create("cloud-init status --wait")
      .build())

    minecraftServer.ipv4Address()
  }
}
