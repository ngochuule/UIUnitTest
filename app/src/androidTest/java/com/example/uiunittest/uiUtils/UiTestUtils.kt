package com.example.uiunittest.uiUtils

import android.app.Activity
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.test.espresso.action.ViewActions
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObjectNotFoundException
import androidx.test.uiautomator.UiSelector
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

class UiTestUtils {
    private lateinit var mActivity: Activity
    private var screensShotCounter =0
    private lateinit var screenShotDir: String
    private val screenShotDirFormat ="yyyyMMddHHmmss"
    private lateinit var filePrefix: String
    private var paramRemoveSuccessScreenShots = true
    private val TAG: String  ="UITest"
    private val charPool: List<Char> =('a'.. 'z') + ('A'..'Z') + ('0'.. '9')

    init {
        updateFilePreFix()
        Log.e(TAG,"Begin Test $filePrefix")
    }

    fun setActivity(activity: Activity){
        this.mActivity = activity
    }

    private fun updateFilePreFix(){
        this.filePrefix = randomString(10)
    }

    private fun setParamRemoveSuccessScreenShots(valToSet: Boolean) {
        this.paramRemoveSuccessScreenShots = valToSet
    }

    private fun randomString(length: Int): String{
        return (1.. length)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    fun allowPermissionsIfNeeded() {
        Log.e(TAG,"allowPermissionsIfNeeded ${Build.VERSION.SDK_INT}")
        if (Build.VERSION.SDK_INT >= 23) {
            this.findObjectPermission()
        }
    }

    private fun findObjectPermission() {
        UiDevice.getInstance(android.support.test.InstrumentationRegistry.getInstrumentation()).also { uiDevice ->
            var allowPermissions = uiDevice.findObject(
                UiSelector().clickable(true).checkable(false).index(1)
            )
            Log.e(TAG,"findObjectPermission ${allowPermissions.exists()}")
            if (allowPermissions.exists()) {
                try {
                    allowPermissions.click()
                    Log.e(TAG,"findObjectPermission allowPermissions.click()}")
                } catch (e: UiObjectNotFoundException) {
                    Log.e(TAG,"[Allow Button Does Not Found]")
                }
            }
        }
    }

    private fun captureScreenshot(path: File){
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).also {
            it.takeScreenshot(path)
        }
    }

    fun screenShot(type: String ="" , message: String ="(NO MESSAGE)"): String{
        Log.e(TAG,"screenShot")
        screensShotCounter +=1
        var picNumber = String.format("%06d",screensShotCounter)
        var path ="${this.screenShotDir}/Screenshot-${this.filePrefix}-$picNumber-$type.png"
        Log.e(TAG,"Filename: $path")
        var mPath = File(path)

        this.captureScreenshot(mPath)
        val mMessage = "Message = [$message] Path =[$mPath]"
        Log.e(TAG,mMessage)
        while(!mPath.exists()){
            this.sleep("SHR")
        }
        return mPath.toString()
    }

    fun sleep(type: String) {
        var time = 1000
        when(type) {
            "LNG" -> time = 2500
            "NOR" -> time = 1000
            "SHR" -> time = 800
            "VSHR" -> time = 300
        }
        Thread.sleep(time.toLong())

        ViewActions.closeSoftKeyboard()
    }

    fun prepareScreenShot(removeScreenShot: Boolean = true){
        Log.e(TAG,"prepareScreenShot")
        this.setParamRemoveSuccessScreenShots(removeScreenShot)

        val packageName = mActivity.packageName
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern(this.screenShotDirFormat)
        val formatted = current.format(formatter)
//        val sdcard = Environment.getDownloadCacheDirectory()
        val sdcard = mActivity.externalCacheDir
        this.screenShotDir = "$sdcard/uitest/$formatted-$packageName-${randomString(10)}"
        File(this.screenShotDir).mkdirs()
        Log.e(TAG, "📷 saveDirectory=[${this.screenShotDir}]")
    }

    fun removeSuccessScreenShots(){
        if(!this.paramRemoveSuccessScreenShots)
            return
        File("${this.screenShotDir}/").walkTopDown().forEach {
            if(it.name.contains("${this.filePrefix}-.*.png$".toRegex())){
                it.delete()
                while (it.exists()){
                    this.sleep("SHR")
                }
            }
        }
        File("${this.screenShotDir}/").delete()
    }
}

class ScreenShotTakingRule(mUIUtils: UiTestUtils) : TestWatcher(){
    private var mUIUtils = mUIUtils
    private val TAG: String  ="ScreenShotTakingRule"
    override fun failed(e: Throwable?, description: Description?) {
        super.failed(e, description)
        val path = mUIUtils.screenShot("FAIL-$description")
        Log.e(TAG,"TEST FAILED !!! <<< ScreenShot Taken method=[$description] filename=[$path]")
    }
}