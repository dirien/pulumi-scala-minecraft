package io.dirien.minecraft

import com.pulumi.core.Output
import com.pulumi.digitalocean.{Droplet, DropletArgs, SshKey, SshKeyArgs}
import com.pulumi.{Context, Pulumi}
import com.pulumi.command.remote.{Command, CommandArgs}
import com.pulumi.command.remote.inputs.ConnectionArgs
import scala.io.Source

object App {

  final val region = "fra1"
  final val size = "c2-4vcpu-8gb"
  final val image = "ubuntu-22-04-x64"

  def readFile(path: String): String = {
    val file = Source.fromFile(path)
    val fileContent = try file.mkString finally file.close()
    fileContent
  }

  def main(args: Array[String]): Unit = {
    Pulumi.run { (ctx: Context) =>

      val minecraftServer = new MinecraftServer(image, region, size)
      val ip = minecraftServer.infrastructure()

      ctx.`export`("public ip", ip)
    }
  }
}


