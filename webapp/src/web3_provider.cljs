(ns web3-provider)

;(def log (.-log js/console))
(def provider (.-ethereum js/window))

(defn not-installed?[]
  (nil? provider))

(defn eth-rpc[method & params]
  (->
    (.send provider (str "eth_" (name method)) (clj->js params))
    (.then #(.-result %))))

;;token Avastar
;;0x30e011460ab086a0daa117df3c87ec0c283a986e
;;token weth
;;0x8dd25714ccece48767baf266ec4f220b60f84d52
