(ns eth-util
  (:require [abi]
            [clojure.spec.alpha :as s]))

(def provider (.-ethereum js/window))
(def zero-address "0x0000000000000000000000000000000000000000")
(def nil-result "0x")

(defn not-installed?[]
  (nil? provider))

(s/fdef eth-rpc
        :args (s/cat :method keyword? :params (s/* some?))
        :ret any?)
(defn eth-rpc 
  ;wrapper around send() JSON rpc method of web3 provider
  ([method & params] (eth-rpc :eth method params))
  ([module method & params]
  (->
    (.send provider (str (name module) "_" (name method)) (clj->js params))
    (.then #(.-result %)))))

;;token Avastar
;;0x30e011460ab086a0daa117df3c87ec0c283a986e
;;token weth
;;0x8dd25714ccece48767baf266ec4f220b60f84d52

(defn valid-address?[address]
  (re-matches #"^0x[0-9a-fA-F]{40}$" address))

(defn get-connection[] 
  (->(.all js/Promise
           [(-> (eth-rpc :requestAccounts)
                (.then #(first %)))
            (eth-rpc :net :version)]))

(defn get-token-balance[token account]
  (eth-rpc :call {:from account :to token :data (abi/encode-call :erc20/balanceOf account)})
  (.then #(if (= nil-result %) nil %)))

(defn get-erc20-props[token]
  (let[req {:from zero-address :to token}
       get-name (assoc req :data (abi/encode-call :erc20/name))
       get-symbol (assoc req :data (abi/encode-call :erc20/symbol))]
  (->(.all js/Promise
           [(-> (eth-rpc :call get-name)
                (.catch (constantly nil)))
            (-> (eth-rpc :call get-symbol)
                (.catch (constantly nil)))]))))
           
