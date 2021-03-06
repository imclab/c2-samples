(ns nested-barchart
  (:use [c2.core :only [unify]]
		[c2.svg :only [translate]])
  (:require [c2.scale :as scale]))

(def css "
body { background-color:white;}
.total { fill:grey; opacity: 0.2; stroke:none;}
.male { fill:blue; stroke:none;}
.female { fill:pink; stroke:none;}
text { fill:grey; font-family:serif; stroke:none}
line { stroke: black;}
")

(let [data [{:label "A" :m 5 :f 13} {:label "B" :m 8 :f 4} {:label "C" :m 7 :f 11} {:label "D" :m 11 :f 9}]
      s (scale/linear :domain [0 15] :range [0 300])
	  indexed-data (map-indexed vector data)]
  [:svg
	[:style {:type "text/css"} (str "<![CDATA[" css "]]>")]
	[:g.chart {:transform (translate [50 50])}
		[:g.total
			(unify indexed-data (fn [[idx data-point]]
				[:rect {:x (* idx 25) :y (- 300 (+ (s (:m data-point)) (s (:f data-point)))) :width 20 :height (+ (s (:m data-point)) (s (:f data-point)))}]))]
		[:g.male
			(unify indexed-data (fn [[idx data-point]]
				[:rect {:x (* idx 25) :y (- 300 (s (:m data-point))) :width 10 :height (s (:m data-point))}]))]
		[:g.female
			(unify indexed-data (fn [[idx data-point]]
				[:rect {:x (+ (* idx 25) 10) :y (- 300 (s (:f data-point))) :width 10 :height (s (:f data-point))}]))]]
	[:g.labels {:transform (translate [50 350])}
		(unify indexed-data (fn [[idx data-point]]
			[:text {:x (* idx 25) :y 18} (:label data-point)]))]
	[:g.axes {:transform (translate [30 350])}
		[:line {:x1 18 :y1 0 :x2 118 :y2 0}]
		[:line {:x1 18 :y1 (- 350) :x2 18 :y2 0}]
		(unify [100 200 300] (fn [tick]
			[:line {:x1 12 :y1 (- tick) :x2 18 :y2 (- tick)}]))
		(unify [100 200 300] (fn [tick]
			[:text {:x -20 :y (- tick)} (str tick)]))]
])