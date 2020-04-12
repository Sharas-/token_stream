(ns presentation.components.disabled-panel)

(defn disabled-panel[content]
  [:div {:style {:pointer-events "none" :opacity "0.4"}} 
   content])
