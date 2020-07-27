(ns examples.atom-snake
  (:import [java.awt Color Dimension]
           [java.awt.event ActionListener KeyEvent KeyListener]
           [javax.swing JFrame JOptionPane JPanel Timer]))

; --------------------
; Functional Model
; --------------------

(def width 20)

(def height 20)

(def point-size 15)

(def turn-millis 150)

(def win-length 5)

(def dirs {KeyEvent/VK_LEFT  [-1 0]
           KeyEvent/VK_RIGHT [1 0]
           KeyEvent/VK_UP    [0 -1]
           KeyEvent/VK_DOWN  [0 1]})

(defn add-points [& pts]
  (vec (apply map + pts)))

(defn point-to-screen-rect [pt]
  (map #(* point-size %) [(pt 0) (pt 1) 1 1]))

(defn create-apple []
  {:location [(rand-int width) (rand-int height)]
   :color    (Color. 210 50 90)
   :type     :apple})

(defn create-snake []
  {:body  (list [1 1])
   :dir   [1 0]
   :color (Color. 15 160 70)
   :type  :snake})

(defn move [{:keys [body dir] :as snake} & grow]
  (assoc snake :body (cons (add-points (first body) dir)
                           (if grow body (butlast body)))))

(defn win? [{body :body}]
  (>= (count body) win-length))

(defn head-overlaps-body? [{[head & body] :body}]
  (contains? (set body) head))

(def lose? head-overlaps-body?)

(defn eats? [{[snake-head] :body} {apple :location}]
  (= snake-head apple))

(defn turn [snake newdir]
  (assoc snake :dir newdir))

; --------------------
; Mutable Model
; --------------------

(def test-snake (ref nil))

(def test-apple (ref nil))

(defn reset-game [game]
  (merge game {:apple (create-apple) :snake (create-snake)}))

(defn update-direction [{snake :snake :as game} newdir]
  (merge game {:snake (turn snake newdir)}))

(defn update-positions [{snake :snake apple :apple :as game}]
  (if (eats? snake apple)
    (merge game {:apple (create-apple) :snake (move snake :grow)})
    (merge game {:snake (move snake)})))

; --------------------
; GUI
; --------------------

(defn fill-point [g pt color]
  (let [[x y width height] (point-to-screen-rect pt)]
    (.setColor g color)
    (.fillRect g x y width height)))

(defmulti paint (fn [_ object & _] (:type object)))

(defmethod paint :apple [g {:keys [location color]}]
  (fill-point g location color))

(defmethod paint :snake [g {:keys [body color]}]
  (doseq [point body]
    (fill-point g point color)))

(defn game-panel [frame game]
  (proxy [JPanel ActionListener KeyListener] []
    (paintComponent [g]
      (proxy-super paintComponent g)
      (paint g (@game :snake))
      (paint g (@game :apple)))
    (actionPerformed [_]
      (swap! game update-positions)
      (when (lose? (@game :snake))
        (swap! game reset-game)
        (JOptionPane/showMessageDialog frame "You lose!"))
      (when (win? (@game :apple))
        (swap! game reset-game)
        (JOptionPane/showMessageDialog frame "You win!"))
      (.repaint this))
    (keyPressed [e]
      (swap! game update-direction (dirs (.getKeyCode e))))
    (getPreferredSize []
      (Dimension. (* (inc width) point-size)
                  (* (inc height) point-size)))
    (keyReleased [_])
    (keyTyped [_])))

(defn game []
  (let [game (atom (reset-game {}))
        frame (JFrame. "Snake")
        panel (game-panel frame game)
        timer (Timer. turn-millis panel)]
    (doto panel
      (.setFocusable true)
      (.addKeyListener panel))
    (doto frame
      (.add panel)
      (.pack)
      (.setVisible true))
    (.start timer)
    [game timer]))
