## Toolkit

---

<code>

    implementation 'com.github.XiaoXin956.Toolkit:network:2.0.7'

    implementation 'com.github.XiaoXin956.Toolkit:basic:2.0.7'

</code>

### 常用工具封装

### basic

### map

### network

* 说明
    * CustomInterceptors 自定义拦截器
    * method 文件夹-> 分别是请求参数、get、post 请求方法的封装
    * IAPIService 请求接口
    * RetrofitManager 请求管理器
    * RetrofitManagerDownLoad 文件下载请求
    * UploadFileRequestBody 文件上传的 RequestBody
    *

* 简单用法

```
val api = RetrofitManager().getInstance()
val maps = mapOf(key to value)
  api.postMethod .setUrl(Constant.OrderScanType)
  .addHeader("access-token", token)
  .setDataMap(maps)
  .requestT(
  success = { data ->
  // 解析 }, 
  error = { error ->
  // 解析 }
  )
```
* 自定义拦截器

```
private val okHttpClient: OkHttpClient by lazy {
      OkHttpClient.Builder()
          .addInterceptor(LoginInterceptor())
          .build()
}

val api by lazy {
    RetrofitManager.Builder()
        .setBaseUrl("")
        .setGson(GsonBuilder().create())
        .setOkHttpClient(okHttpClient)
        .build()
        .getInstance()
}

val maps = mapOf(key to value)
  api.postMethod .setUrl(Constant.OrderScanType)
  .addHeader("access-token", token)
  .setDataMap(maps)
  .requestT(
  success = { data ->
  // 解析 }, 
  error = { error ->
  // 解析 }
  )
```

### pay


