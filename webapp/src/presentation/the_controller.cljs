(ns presentation.the-controller
  (:require [reagent.dom :as rdom]
            [reagent.core :as r]
            [eth-util :as eth]
            [presentation.components :refer [token-search status] :rename {status token-status}]))

(def status (js/document.getElementById "status"))
(def content (js/document.getElementById "main"))

(def vm_error (r/atom ""))
(def vm_connection (r/atom {:account "" :network ""}))
(def vm_token (r/atom {:balance "" :name "" :symbol ""}))

(defn render[container content]
  (rdom/render content container))

(defn connect&run[step_fn] 
  ()
  (let[promissed-steps 
       (loop[[[step_fn & args] vm] (first steps)]
         (-> (apply step_fn args)
             (.then #(reset! vm)))
         (recur (rest steps)))]
    (.catch promissed-steps #(reset! error %)))
  
(defn on-token-entered[token-address]
  (if (not (eth/valid-address? token-address))
    (reset! error (str "invalid address: " token-address))
    (do-steps [[] connection]
              [eth/request-connection] connection] ]
      (-> (eth/request-connection)
          (.then #(eth/get-token-balance % token-address)
          (.then #(if))
          (.then #(eth/get-erc20-props token-address)
          (.catch #(log :error %))))))

(defn ^:export main[]
  (js/window.addEventListener "error" #(render status (str (js->clj %))))
  (js/window.addEventListener "unhandledrejection" #(render status (str (js->clj %))))
  (if (eth/not-installed?)
    (reset! error "Ethereum provider is not installed. Please install it to continue.")
   ;else
    (do (render status [error vm_error])
        (render content [[connection vm_connection]
                         [token-search vm_token on-token-entered]]))))
