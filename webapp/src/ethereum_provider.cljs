(ns ethereum-provider
  (:require [ethers]))

(def eth-provider (.-ethereum js/window))

(def erc20-abi (clj->js ["function balanceOf(address owner) view returns (uint)"
                         "function name() view returns (string)"
                         "function symbol() view returns (string)"]))

(defn not-installed?[]
  (nil? eth-provider))

(defn connect[on-connected, on-disconnected, network]
  (def eth-client (new ethers/providers.Web3Provider eth-provider, network))
  (let [on-accounts-changed #(if (empty? %) 
                               (on-disconnected)
                               (on-connected (first %)))]
    (.on eth-provider "accountsChanged" on-accounts-changed)
    (-> (.send eth-provider "eth_requestAccounts")
        (.then #(on-accounts-changed (.-result %))))))

(defn address-invalid?[address]
  (try
    (ethers/utils.getAddress address)
    false
    (catch :default e 
      true)))

(defn get-ether-balance[account-id]
   (-> (.getBalance eth-client account-id)
       (.then #(ethers/utils.formatEther %))))

(defn send-ehter-to[recipient-id amount]
  (let [valid-address (ethers/utils.getAddress recipient-id)
        value (ethers/utils.parseEther amount)
        transaction (clj->js {:to valid-address :value value})
        signer (-> eth-client .getSigner)]
    (-> signer .sendTransaction transaction)))

;token Avastar
;0x30e011460ab086a0daa117df3c87ec0c283a986e
;token weth
;0x8dd25714ccece48767baf266ec4f220b60f84d52
(defn make-erc20[token-id]
  (let [valid-address (ethers/utils.getAddress token-id)]
    (new ethers/Contract valid-address erc20-abi eth-client)))
