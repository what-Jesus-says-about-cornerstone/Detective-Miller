(ns Detective-Miller.runtime.ut-metadata
  (:require
   [clojure.core.async :as a :refer [chan go go-loop <! >!  take! put! offer! poll! alt! alts! close! onto-chan!
                                     pub sub unsub mult tap untap mix admix unmix pipe
                                     timeout to-chan  sliding-buffer dropping-buffer
                                     pipeline pipeline-async]]
   [clojure.spec.alpha :as s]
   [Detective-Miller.protocols :as Detective-Miller.protocols]
   [Detective-Miller.spec :as Detective-Miller.spec])
  (:import
   (java.io ByteArrayOutputStream ByteArrayInputStream PushbackInputStream)
   (bt.metainfo TorrentId)))

(do (set! *warn-on-reflection* true) (set! *unchecked-math* true))

(defn create
  [{:as opts
    :keys [::send|
           ::recv|
           ::metadata|
           ::Detective-Miller.spec/infohashBA
           ::Detective-Miller.spec/peer-idBA]}]
  {:pre [(s/assert ::Detective-Miller.spec/create-wire-opts opts)]
   :post [(s/assert ::Detective-Miller.spec/wire %)]}
  (let [stateV (volatile!
                {})

        ex| (chan 1)

        expected-size| (chan 1)
        cut| (chan 1)

        wire
        ^{:type ::wire}
        (reify
          Detective-Miller.protocols/Wire
          clojure.lang.IDeref
          (deref [_] @stateV))

        release (fn []
                  (close! expected-size|))]))