(ns presentation.components.token-search)

(defn token-search[on-token-entered, view-model]
  [:p 
    [:label {:for "token-address"} "token address"]
    [:input#token-address {:type "text" :onKeyPress #(when (some #{13} [(.-keyCode %) (.-which %)])
                                          (let [token-id (-> % .-target .-value)]
                                            (on-token-entered token-id)))}]
    [:span @view-model]])

