(ns ^{:author "Sebastian Nowicki"
      :doc "Bitcoinchart HTTP API client implementation."}
  bitcoincharts.client.http
  (:use [clojure.string :only (join)])
  (:require [clj-http.client :as http]
            [clojure.data.csv :as csv]))

(def base-url "http://bitcoincharts.com/t")
(defn- trades-url [ticker] (str "trades.csv?symbol=" ticker))

(defn- api-request
  [resource]
  (http/get (join "/" [base-url resource])))

(defn- make-trade-from-api
  [[unixtime price amount]]
  {:unixtime (Integer/parseInt unixtime)
   :price (Float/parseFloat price)
   :amount (Float/parseFloat amount)})

(defn historic-data
  "Retrieves a list of historic trades for a specific ticker. Each trade
  consists of a :unixtime, :price and :amount."
  [ticker]
  (let [response (api-request (trades-url ticker))]
    (map make-trade-from-api (csv/read-csv (get response :body)))))

