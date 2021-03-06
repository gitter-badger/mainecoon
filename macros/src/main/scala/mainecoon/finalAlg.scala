/*
 * Copyright 2017 Kailuo Wang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package mainecoon

import scala.annotation.StaticAnnotation
import Util._
import scala.meta._
import collection.immutable.Seq

class finalAlg extends StaticAnnotation {
  inline def apply(defn: Any): Any = meta {

    def newMethods(ad: AlgDefn): Seq[Defn] = {
      import ad._
      import cls._

      Seq(
        q"def apply[..${cls.tparams}](implicit inst: $name[..${tArgs()}]): $name[..${tArgs()}] = inst",
        q"""implicit def autoDeriveFromFunctorK[${effectType}, G[_], ..${extraTParams}](
              implicit af: $name[..${tArgs()}],
              FK: _root_.mainecoon.FunctorK[$typeLambdaForFunctorK],
              fk: _root_.cats.~>[F, G])
              : $name[..${tArgs("G")}] = FK.mapK(af)(fk)
         """)
    }
    enrichAlg(defn)(newMethods)
  }
}
