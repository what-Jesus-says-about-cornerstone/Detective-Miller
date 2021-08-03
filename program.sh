#!/bin/bash

repl(){
  clj \
    -X:repl deps-repl.core/process \
    :main-ns canterbury.main \
    :port 7788 \
    :host '"0.0.0.0"' \
    :repl? true \
    :nrepl? false
}


main(){
  clojure \
    -J-Dclojure.core.async.pool-size=1 \
    -J-Dclojure.compiler.direct-linking=false \
    -M -m canterbury.main
}

uberjar(){
  clojure \
    -X:uberjar hf.depstar/uberjar \
    :aot true \
    :jar out/canterbury.standalone.jar \
    :verbose false \
    :main-class canterbury.main
  mkdir -p out/jpackage-input
  mv out/canterbury.standalone.jar out/jpackage-input/
}

"$@"