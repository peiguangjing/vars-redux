package org.mbari.math

import org.slf4j.LoggerFactory

/**
 *
 * @author Brian Schlining
 * @since Aug 31, 2010
 */
class CoallateFunction {

    private static final log = LoggerFactory.getLogger(CoallateFunction.getClass())

    static final DO_NOTHING_CLOSURE = { it }

    static coallate(List d0, List d1, offset) {
        coallate(d0, DO_NOTHING_CLOSURE, d1, DO_NOTHING_CLOSURE, offset)
    }

    /**
     * Coallates 2 different sets together based on the field you indicate. You
     * can apply a transform to each field so that they can be intercompared. For
     * example, if you want to compare timecode with time. The returned map uses
     * values form d0 as the key and the nearest value from d1, within offset distance,
     * for the value. If no value in d1 is within offset of a value within d0 then it
     * will not be included in the returned list
     *
     * @param d0 The list of 'key' objects that you wish to coallate to
     * @param transform0 A transform applied to field0 to convert it to some numeric value (if needed)
     * @param d1 The list of value objects that will be coallated to d0
     * @param transform1 A transform applied to field1 to convert it to some numeric value (if needed)
     * @param offset A cutoff value such that if no match is found between val1 and val2 that's within offset
     *      no value will be returned for that d0 record.
     * @return a Map of 
     *
     */
    static coallate(List d0, Closure transform0, List d1, Closure transform1, offset) {
        def data = [:]

        def list0 = d0.sort(transform0)         // Sorted d0
        def list1 = d1.sort(transform1)        // Sorted d1

        def vals0 = list0.collect(transform0) // transformed d0 in same order as vals0
        def vals1 = list1.collect(transform1) // transformed d1 in same order as vals1


        int i = 0
        vals0.eachWithIndex { val0, idx ->
            def t0 = val0
            def dtBest = offset

            def goodDatum = null
            for (row in i..<list1.size()) {
                def val1 = vals1[row]
                def t1 = val1
                def dt = Math.abs(t0 - t1)
                if (dt <= dtBest) {
                    dtBest = dt
                    i = row
                    goodDatum = val1
                }
                else if ((dt > dtBest) && (t1 > t0)) {
                    break
                }
            }

            if (goodDatum) {
                data[list0[idx]] = list1[i]
            }
            else {
                log.debug("No record was found to match ${val0}")
            }
        }
        return data
    }

}

