(ns presentation.components.ethereum-connection)

(defn ethereum-connection[account network balance]
  [:span "account: " account " network: " network " balance: " balance])
