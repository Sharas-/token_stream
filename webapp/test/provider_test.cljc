(ns provider-test
  (:require [web3-provider]
            [abi]
            [cljs.test :refer-macros [is deftest are testing use-fixtures async]]))

(enable-console-print!)

(deftest provider-gets-token-balance-test
  (let [token "0x8dd25714ccece48767baf266ec4f220b60f84d52"
        account "0xE0648BE4e939Fa82900F9A80E8EE39F5d5B7bb8c"
        transaction {:from account :to token :data (abi/encode-call :balanceOf account)}]
    (async done
      (-> (web3-provider/eth-rpc :call transaction)
      (.then #(is %)))
      (done))))

(deftest provider-gets-ether-balance-test
  (let [account "0xE0648BE4e939Fa82900F9A80E8EE39F5d5B7bb8c"]
    (async done
      (-> (web3-provider/eth-rpc :getBalance account)
      (.then #(is %)))
      (done))))

(defmethod cljs.test/report [:cljs.test/default :end-run-tests] [m]
  (if (cljs.test/successful? m)
    (print "Success!")
    (print "bledstva na parachode")))

