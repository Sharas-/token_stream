(ns myrepl
   (:require [eth-util :as e]
             [ethereumjs-util :as eu]
             [abi]))

(enable-console-print!)

(defn print-ret[arg]
  (print arg)
  arg)

;(print-ret (eu/toUtf8(eu/unpad "0x000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000000000000000000000000000000000000000045745544800000000000000000000000000000000000000000000000000000000")))
;
;(print-ret (eu/toUtf8(eu/unpad "0x0000000000000000000000000000000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000000d5772617070656420457468657200000000000000000000000000000000000000")))
(defn log-send [method & params]
  (print "-- invoked -- " method)
  (-> (.send e/provider (str "eth_" (name method)) (clj->js params))
      (.then print-ret)
      (.catch print-ret)))

;(log-send :requestAccounts)
;(log-send :call {:from "0xE0648BE4e939Fa82900F9A80E8EE39F5d5B7bb8c"  :to "0x8dd25714ccece48767baf266ec4f220b60f84d52" 
;                 :data (abi/encode-call :erc20/balanceOf "0xE0648BE4e939Fa82900F9A80E8EE39F5d5B7bb8c")})
;
(def weth-rinkeby "0x8dd25714ccece48767baf266ec4f220b60f84d52")
(def weth-mainnet "0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2")
(def account1 "0xE0648BE4e939Fa82900F9A80E8EE39F5d5B7bb8c")
(def account2 "0xAd658AF200E9De9FCa2Bfb352f2e490e5ACc13DF")

;(print-ret (str "0x" (.toString 1000000 16)))
;(let [tr {:from account1 :to weth-rinkeby :value (str "0x" (.toString 1000000 16))}]
;(log-send :sendTransaction tr))

;(let [transaction {:from "0xE0648BE4e939Fa82900F9A80E8EE39F5d5B7bb8c" :to "0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2"}
;      get-name (assoc transaction :data (abi/encode-call :erc20/name))
;      get-symbol (assoc transaction :data (abi/encode-call :erc20/symbol))]
;  (->(.all js/Promise
;           [(-> (log-send :call get-name)
;                (.catch (constantly nil)))
;            (-> (log-send :call get-symbol)
;                (.catch (constantly nil)))])
;           (.then print-ret))))
;   
(->(e/get-token-balance weth-rinkeby account1)
(.then #(print-ret(str "---balance: " (js/parseInt % 16)))))

