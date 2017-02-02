package io.iohk.ethereum.utils

import java.net.InetSocketAddress

import akka.util.ByteString
import com.typesafe.config.ConfigFactory
import org.spongycastle.util.encoders.Hex

import scala.collection.JavaConversions._
import scala.concurrent.duration._

object Config {

  private val config = ConfigFactory.load().getConfig("etc-client")

  val clientId = config.getString("client-id")

  object Network {
    private val networkConfig = config.getConfig("network")

    val networkId = networkConfig.getInt("network-id")

    object Server {
      private val serverConfig = networkConfig.getConfig("server-address")

      val interface = serverConfig.getString("interface")
      val port = serverConfig.getInt("port")
      val listenAddress = new InetSocketAddress(interface, port)
    }

    object Discovery {
      private val discoveryConfig = networkConfig.getConfig("discovery")

      val bootstrapNodes = discoveryConfig.getStringList("bootstrap-nodes").toList
    }

    object Peer {
      private val peerConfig = networkConfig.getConfig("peer")

      val connectRetryDelay = peerConfig.getDuration("connect-retry-delay").toMillis.millis
      val connectMaxRetries = peerConfig.getInt("connect-max-retries")
    }

  }

  object Blockchain {
    private val blockchainConfig = config.getConfig("blockchain")

    val genesisDifficulty = blockchainConfig.getLong("genesis-difficulty")
    val genesisHash = ByteString(Hex.decode(blockchainConfig.getString("genesis-hash")))
  }

}