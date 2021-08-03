(ns canterbury.runtime.ut-metadata
  (:require
   [clojure.core.async :as a :refer [chan go go-loop <! >!  take! put! offer! poll! alt! alts! close! onto-chan!
                                     pub sub unsub mult tap untap mix admix unmix pipe
                                     timeout to-chan  sliding-buffer dropping-buffer
                                     pipeline pipeline-async]]
   [clojure.spec.alpha :as s]
   [canterbury.protocols :as canterbury.protocols]
   [canterbury.spec :as canterbury.spec])
  (:import
   (java.io ByteArrayOutputStream ByteArrayInputStream PushbackInputStream)
   (bt.metainfo TorrentId)))

(do (set! *warn-on-reflection* true) (set! *unchecked-math* true))

(defn create
  [{:as opts
    :keys [::send|
           ::recv|
           ::metadata|
           ::canterbury.spec/infohashBA
           ::canterbury.spec/peer-idBA]}]
  {:pre [(s/assert ::canterbury.spec/create-wire-opts opts)]
   :post [(s/assert ::canterbury.spec/wire %)]}
  (let [stateV (volatile!
                {})

        ex| (chan 1)

        expected-size| (chan 1)
        cut| (chan 1)

        wire
        ^{:type ::wire}
        (reify
          canterbury.protocols/Wire
          clojure.lang.IDeref
          (deref [_] @stateV))

        release (fn []
                  (close! expected-size|))]))