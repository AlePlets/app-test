package com.app.lillup.utils

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Lifecycle
import com.app.lillup.passport.activities.PassportActivity
import com.web3auth.core.Web3Auth
import com.web3auth.core.types.LoginConfigItem
import com.web3auth.core.types.LoginParams
import com.web3auth.core.types.Network
import com.web3auth.core.types.Provider
import com.web3auth.core.types.TypeOfLogin
import com.web3auth.core.types.Web3AuthOptions
import com.web3auth.core.types.Web3AuthResponse
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.await
import kotlinx.coroutines.launch
import org.bouncycastle.util.test.FixedSecureRandom
import org.web3j.crypto.Credentials
import org.web3j.crypto.Hash
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.Sign
import org.web3j.crypto.TransactionEncoder
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.response.EthChainId
import org.web3j.protocol.core.methods.response.EthGetBalance
import org.web3j.protocol.core.methods.response.EthGetTransactionCount
import org.web3j.protocol.core.methods.response.EthSendTransaction
import org.web3j.protocol.http.HttpService
import org.web3j.utils.Convert
import org.web3j.utils.Numeric
import java.math.BigInteger
import java.nio.charset.StandardCharsets

private const val packageName = "com.app.lillup"
private val rpcUrl = "https://rpc.ankr.com/eth_goerli"; // Goerli Testnet
//private val rpcUrl = "https://rpc.public.zkevm-test.net"; // Goerli Testnet



object WebAuthHelper {
    var web3Auth :Web3Auth?=null
    var credentials: Credentials?=null
    var web3: Web3j?=null
    fun initAuth(context:Context,intent:Intent,id:String){
        web3Auth = Web3Auth(
            Web3AuthOptions(
                context = context,
                clientId = id,
                network = Network.TESTNET,
                redirectUrl = Uri.parse("$packageName://auth"), // your app's redirect URL
//                loginConfig = hashMapOf("google" to LoginConfigItem(
//                    verifier = "ppp-lillup-test", // get it from web3auth dashboard
//                    typeOfLogin = TypeOfLogin.GOOGLE,
//                    clientId = ClineId // google's client id
//                )
//                )
            )
        )

// Handle user signing in when app is not alive
//        web3Auth?.setResultUrl(Uri.parse("$packageName://auth"))
        web3Auth?.setResultUrl(intent.data)
    }
    fun login(onComplete: (Web3AuthResponse) -> Unit,onError: (String?) -> Unit,onProgress: (Int) -> Unit,){
        val selectedLoginProvider = Provider.GOOGLE

        web3Auth?.let {
            val loginCompletableFuture: CompletableFuture<Web3AuthResponse> = it.login(LoginParams(selectedLoginProvider))

            // ScheduledExecutorService for periodic progress reporting
            val executorService = Executors.newSingleThreadScheduledExecutor()

            // Schedule a task to report progress every 100 milliseconds
            val progressReportingTask = executorService.scheduleAtFixedRate({
                // Replace this with your actual logic to calculate and report progress
                val progress = calculateProgress(loginCompletableFuture)
                onProgress(progress)
                println("Progress: $progress%")
            }, 0, 100, TimeUnit.MILLISECONDS)

            loginCompletableFuture
                .thenApplyAsync { loginResponse ->
                    // Task completed successfully
                    println("LOGIN_DATA  ${loginResponse.error}")
                    println("LOGIN_DATA  ${loginResponse.sessionId}")
                    println("LOGIN_DATA  ${loginResponse.coreKitKey}")
                    println("LOGIN_DATA  ${loginResponse.privKey}")
                    println("LOGIN_DATA  ${loginResponse.ed25519PrivKey}")
                    println("LOGIN_DATA  ${loginResponse.userInfo?.email}")
                    println("LOGIN_DATA  ${loginResponse.userInfo?.typeOfLogin}")
                    println("LOGIN_DATA  ${loginResponse.userInfo?.idToken}")
                    println("LOGIN_DATA  ${loginResponse.userInfo?.dappShare}")
                    onComplete(loginResponse);
                }
                .exceptionally { error ->
                    // Task completed with an exception (error)
                    // Render error UI
                    onError(error.message);
                    null
                }
                .whenComplete { _, _ ->
                    // Cancel the progress reporting task when the CompletableFuture completes
                    progressReportingTask.cancel(true)

                    // Shutdown the executor service
                    executorService.shutdown()
                }
        }


    }
    fun loginV1(){
        val selectedLoginProvider = Provider.GOOGLE

        val loginCompletableFuture: CompletableFuture<Web3AuthResponse> = web3Auth!!.login(LoginParams(selectedLoginProvider))


        loginCompletableFuture.thenApply { loginResponse ->
            if (loginResponse.error != null) {
                Log.e("loginV1","LOGIN_DATA Error:thenApplyAsync  ${loginResponse.error}")
            }else{
                Log.e("loginV1","LOGIN_DATA  thenApplyAsync  ${loginResponse.sessionId}")
                Log.e("loginV1","LOGIN_DATA  thenApplyAsync  ${loginResponse.coreKitKey}")
                Log.e("loginV1","LOGIN_DATA  thenApplyAsync ${loginResponse.privKey}")
                Log.e("loginV1","LOGIN_DATA  thenApplyAsync  ${loginResponse.ed25519PrivKey}")
                Log.e("loginV1","LOGIN_DATA  thenApplyAsync  ${loginResponse.userInfo?.email}")
                Log.e("loginV1","LOGIN_DATA  thenApplyAsync  ${loginResponse.userInfo?.typeOfLogin}")
                Log.e("loginV1","LOGIN_DATA  thenApplyAsync  ${loginResponse.userInfo?.idToken}")
                Log.e("loginV1","LOGIN_DATA  thenApplyAsync ${loginResponse.userInfo?.dappShare}")
            }
            loginResponse
        }.whenComplete { loginResponse, error ->
            if (error == null) {
                // render logged in UI
                Log.e("loginV1","LOGIN_DATA  Error ${error}")
                return@whenComplete
            }

            Log.e("loginV1","LOGIN_DATA  ${loginResponse.sessionId}")
            Log.e("loginV1","LOGIN_DATA  ${loginResponse.coreKitKey}")
            Log.e("loginV1","LOGIN_DATA  ${loginResponse.privKey}")
            Log.e("loginV1","LOGIN_DATA  ${loginResponse.ed25519PrivKey}")
            Log.e("loginV1","LOGIN_DATA  ${loginResponse.userInfo?.email}")
            Log.e("loginV1","LOGIN_DATA  ${loginResponse.userInfo?.typeOfLogin}")
            Log.e("loginV1","LOGIN_DATA  ${loginResponse.userInfo?.idToken}")
            Log.e("loginV1","LOGIN_DATA  ${loginResponse.userInfo?.dappShare}")
        }.exceptionally {
            Log.e("loginV1","LOGIN_DATA  ${it.message}")
            null
        }
    }

    fun loginV2(callback:()->Unit) {
        val selectedLoginProvider = Provider.GOOGLE
        web3Auth!!.initialize().thenApplyAsync {v->
            CoroutineScope(Dispatchers.Default).launch {
                try {
                    web3Auth?.let {

                        Log.e("loginV1","Before login call")

                        val loginResponse = it.login(LoginParams(selectedLoginProvider)).await()

                        Log.e("loginV1", "LoginResponse ${loginResponse}\n\n\n");

                        credentials = Credentials.create(loginResponse.sessionId)

                        web3 = Web3j.build(HttpService(rpcUrl))
                        callback.invoke();
                        val sign = signMessage("Wallet Initaillize message")
                        Log.e("loginV1", "Address ${credentials!!.address}\n" +
                                "Key Pair privateKey${credentials!!.ecKeyPair.privateKey}\n" +
                                "Key Pair publicKey ${credentials!!.ecKeyPair.publicKey}\n" +
                                "Balance ${getBalance()}\n" +
                                "VerifierId ${loginResponse.userInfo?.verifierId}" +
                                "Message ${sign}\n");
                        val sendTransaction = sendTransaction(200.0, credentials!!.address)

                    }
                } catch (e: CancellationException) {
                    // Handle cancellation (e.g., if coroutine is cancelled)
                    Log.e("loginV1", "Coroutine was cancelled: $e")

                }catch (e: Exception) {
                    // Handle exception
                    Log.e("loginV1","LOGIN_DATA Error: $e")
                }
        };

        }
    }
    fun calculateProgress(loginCompletableFuture: CompletableFuture<Web3AuthResponse>): Int {
        // Check if the CompletableFuture is already completed
        if (loginCompletableFuture.isDone) {
            return 100 // Operation is complete, progress is 100%
        }

        // Otherwise, calculate progress based on completion percentage
        val progress = (loginCompletableFuture.numberOfDependents / loginCompletableFuture.thenApply { 1 }.numberOfDependents.toDouble() * 100).toInt()

        // Ensure progress is within the range [0, 100]
        return progress.coerceIn(0, 100)
    }
    private fun getBalance(): BigInteger? {
        val publicAddress = credentials!!.address
//        val publicAddress = "0x7f47DE78CC22Afbb3b3ceA5a65b59B19e5B3490d"
        val ethBalance: EthGetBalance = web3!!.ethGetBalance(publicAddress, DefaultBlockParameterName.LATEST).sendAsync().get()
        println("Balance: ${ethBalance.balance}")
        return ethBalance.balance
    }
    private fun signMessage(message: String): String {
        val startTime = System.currentTimeMillis()
        val hashedData = Hash.sha3(message.toByteArray(StandardCharsets.UTF_8))
        val signature = Sign.signMessage(hashedData, credentials!!.ecKeyPair)
        val r = Numeric.toHexString(signature.r)
        val s = Numeric.toHexString(signature.s).substring(2)
        val v = Numeric.toHexString(signature.v).substring(2)
        val signHash = StringBuilder(r).append(s).append(v).toString()
        val endTime = System.currentTimeMillis()
        val executionTime = endTime - startTime
        val executionTimeSeconds = executionTime / 1000.0
        Log.e("loginV1", "Total Time ${executionTimeSeconds}")
        Log.e("loginV1","Signed Hash: $signHash")
        return signHash
    }
    private fun sendTransaction(amount: Double, recipientAddress: String): String? {
        val ethGetTransactionCount: EthGetTransactionCount = web3!!.ethGetTransactionCount(credentials!!.address, DefaultBlockParameterName.LATEST).sendAsync().get()
        val nonce: BigInteger = ethGetTransactionCount.transactionCount
        val value: BigInteger = Convert.toWei(amount.toString(), Convert.Unit.ETHER).toBigInteger()
        val gasLimit: BigInteger = BigInteger.valueOf(21000)
        val chainId: EthChainId = web3!!.ethChainId().sendAsync().get()

        Log.e("loginv1","Chain Id ${chainId.result} ==== ${chainId.chainId}")

        // Raw Transaction
        val rawTransaction: RawTransaction = RawTransaction.createTransaction(
            chainId.chainId.toLong(),
            nonce,
            gasLimit,
            recipientAddress,
            value,
            "",
            BigInteger.valueOf(5000000000),
            BigInteger.valueOf(6000000000000)
        )

        val signedMessage: ByteArray = TransactionEncoder.signMessage(rawTransaction, credentials)
        val hexValue: String = Numeric.toHexString(signedMessage)
        Log.e("loginv1","Tx Teansection Hash: ${hexValue}")
        val ethSendTransaction: EthSendTransaction = web3!!.ethSendRawTransaction(hexValue).sendAsync().get()
        return if(ethSendTransaction.error != null) {
            Log.e("loginv1","Tx Error: ${ethSendTransaction.error.message}")
            ethSendTransaction.error.message
        } else {
            Log.e("loginv1","Tx Hash: ${ethSendTransaction.transactionHash}")
            ethSendTransaction.transactionHash
        }
    }
}