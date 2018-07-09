package net.mthomassen.airports.web

import akka.actor.ActorSystem
import akka.http.caching.LfuCache
import akka.http.caching.scaladsl.{Cache, CachingSettings}
import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.server.directives.CachingDirectives.cache
import akka.http.scaladsl.server.{Directive0, RequestContext, RouteResult}

import scala.concurrent.duration._

trait CacheHandler {
  implicit val system: ActorSystem

  private[this] val DefaultInitialCapacity = 100
  private[this] val DefaultMaxCapacity = 1000
  private[this] val DefaultITimeToLive = 120 seconds
  private[this] val DefaultITimeToIdle = 60 seconds

  private[this] val keyerFunction: PartialFunction[RequestContext, Uri] = {
    case r: RequestContext ⇒ r.request.uri
  }

  private[this]val defaultCachingSettings = CachingSettings(system)
  private[this]val lfuCacheSettings =
    defaultCachingSettings.lfuCacheSettings
      .withInitialCapacity(DefaultInitialCapacity)
      .withMaxCapacity(DefaultMaxCapacity)
      .withTimeToLive(DefaultITimeToLive)
      .withTimeToIdle(DefaultITimeToIdle)
  private[this]val cachingSettings =
    defaultCachingSettings.withLfuCacheSettings(lfuCacheSettings)
  private[this]val lfuCache: Cache[Uri, RouteResult] = LfuCache(cachingSettings)


  def handleUriCache: Directive0 = cache(lfuCache, keyerFunction)
}
