(ns examples.cryptovault
  (:require [examples.io :refer [gulp expectorate IOFactory make-reader make-writer]]
            [clojure.java.io :as io])
  (:import [java.security KeyStore
                          KeyStore$PasswordProtection
                          KeyStore$SecretKeyEntry]
           [javax.crypto Cipher
                         CipherInputStream
                         CipherOutputStream
                         KeyGenerator]
           [java.io FileInputStream
                    FileOutputStream]))

(defprotocol Vault
  (init-vault [vault])
  (vault-output-stream [vault])
  (vault-input-stream [vault]))

(defn vault-key [vault]
  (let [password (.toCharArray (.password vault))]
    (with-open [fis (FileInputStream. (.keystore vault))]
      (-> (doto (KeyStore/getInstance "JCEKS")
            (.load fis password))
          (.getKey "vault-key" password)))))

(deftype CryptoVault [filename keystore password]
  Vault
  (init-vault [vault]
    (let [password (.toCharArray (.password vault))
          key (.generateKey (KeyGenerator/getInstance "AES"))
          keystore (doto (KeyStore/getInstance "JCEKS")
                     (.load nil password)
                     (.setEntry "vault-key"
                                (KeyStore$SecretKeyEntry. key)
                                (KeyStore$PasswordProtection. password)))]
      (with-open [fos (FileOutputStream. (.keystore vault))]
        (.store keystore fos password))))
  (vault-output-stream [vault]
    (let [cipher (doto (Cipher/getInstance "AES")
                   (.init Cipher/ENCRYPT_MODE (vault-key vault)))]
      (CipherOutputStream. (io/output-stream (.filename vault)) cipher)))
  (vault-input-stream [vault]
    (let [cipher (doto (Cipher/getInstance "AES")
                   (.init Cipher/DECRYPT_MODE (vault-key vault)))]
      (CipherInputStream. (io/input-stream (.filename vault)) cipher)))

  IOFactory
  (make-reader [vault]
    (make-reader (vault-input-stream vault)))
  (make-writer [vault]
    (make-writer (vault-output-stream vault))))

(def vault (->CryptoVault "vault-file" "keystore" "toomanysecrets"))
; => #'examples.cryptovault/vault
(init-vault vault)
; => nil
(expectorate vault "This is a test of the CryptoVault")
; => nil
(gulp vault)
; => "This is a test of the CryptoVault"

(extend CryptoVault
  io/IOFactory
  (assoc io/default-streams-impl
    :make-input-stream (fn [x _] (vault-input-stream x))
    :make-output-stream (fn [x _] (vault-output-stream x))))

(spit vault "This is a test of the CryptoVault using spit and slurp")
; => nil
(slurp vault)
; => "This is a test of the CryptoVault using spit and slurp"
