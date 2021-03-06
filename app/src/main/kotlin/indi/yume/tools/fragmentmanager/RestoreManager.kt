package indi.yume.tools.fragmentmanager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import indi.yume.tools.fragmentmanager.exception.RxStartException
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import java.util.*

/**
 * Created by yume on 17-4-13.
 */

object RestoreManager {
    val savedInstanceStateMap: MutableMap<String, ResultData> = Hashtable<String, ResultData>()

    private val random = Random()

    fun onCreate(savedInstanceState: Bundle?, tag: String): String {
        val data = savedInstanceStateMap[tag] ?: ResultData()
        savedInstanceStateMap.put(tag,
                data.copy(hasSaveSate = false)
        )

        return tag
    }

    fun onSaveInstanceState(tag: String, outState: Bundle?): Bundle? {
        savedInstanceStateMap.mapKey(tag) { it.copy(hasSaveSate = true) }

        return outState
    }

    fun onDestroy(tag: String) {
        if (savedInstanceStateMap.containsKey(tag))
            if (savedInstanceStateMap[tag]?.hasSaveSate ?: false) {
                savedInstanceStateMap.mapKey(tag) { it.copy(hasSaveSate = false) }
            } else {
                val data = savedInstanceStateMap.remove(tag)
                data?.onResultSubject?.onComplete()
            }
    }

//    fun startActivityForObservable(tag: String, activity: Activity, intent: Intent): Single<Pair<Int, Bundle>> {
//        val resultData = savedInstanceStateMap.get(tag) ?: return Single.error(RuntimeException("Do not have this Activity state: tag=" + tag))
//
//        return Single.create { emitter ->
//            val requestCode = random.nextInt() and 0x0000ffff
//            activity.startActivityForResult(intent, requestCode)
//            resultData.onResultSubject.subscribe(
//                    { tuple ->
//                        if (requestCode == tuple.first)
//                            emitter.onSuccess(tuple.second to tuple.third)
//                    },
//                    { emitter.onError(it) })
//        }
//    }

    fun startFragmentForRx(tag: String,
                           stackManager: StackManager,
                           builder: RxStartBuilder): Single<Pair<Int, Bundle>> {
        val intent = builder.getIntent()
        val enableAnimation = builder.isEnableAnimation
        val anim = if (enableAnimation) builder.anim else null

        return Single.create { emitter ->
            val requestCode = random.nextInt() and 0x0000ffff
            val startBuilder = StartBuilder(
                    intent,
                    requestCode,
                    enableAnimation
            ).withAnimData(anim)

            savedInstanceStateMap[tag]!!.onResultSubject
                    .subscribe(
                            { tuple ->
                                if (requestCode == tuple.first)
                                    emitter.onSuccess(tuple.second to tuple.third)
                            },
                            { emitter.onError(it) },
                            { if(!emitter.isDisposed) emitter.onError(RxStartException("Fragment has been destroyed"))})

            stackManager.start(startBuilder)
        }
    }

    fun onResult(tag: String, requestCode: Int, resultCode: Int, data: Bundle?) =
        savedInstanceStateMap[tag]?.onResultSubject?.onNext(Triple(requestCode, resultCode, data ?: Bundle()))
}

fun <K, V> MutableMap<K, V>.mapKey(key: K, mapping: (V) -> V): V? =
        this[key]?.also { this@mapKey.put(key, mapping(it)) }

data class ResultData(
        val onResultSubject: Subject<Triple<Int, Int, Bundle>> = BehaviorSubject.create<Triple<Int, Int, Bundle>>(),
        val hasSaveSate: Boolean = false
)