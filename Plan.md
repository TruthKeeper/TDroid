# Plan

## 功能组件

### EventBus事件总线 widget.event



### Http widget.http

> Retrofit2 + OkHttp3 + RxJava2 + Gson 示例代码见 sample.SampleHttp

- 动态BaseUrl widget.http.RuntimeUrlManager
- Https的支持
- Cookie的管理 widget.http.CookieManager
- 日志打印
- 离线缓存
- 全局监听请求、响应、Glide请求的进度，支持API中动态、静态配置 , 适配重定向 widget.http.progress.ProgressManager

### Image widget.image



### 6.0+权限动态获取 By RxJava2 widget.permission

> PermissionManager.with().request()

## APT

> 编译时生成注解

- 反射实例化工具`com.apt.InstanceFactory`，对支持APT的类添加注解`com.apt.annotation.Instance`

## AOP

> 切面编程

- 日志打印，`@com.tk.tdroiddemo.aop.annotation.Logger`，修饰类、构造方法、方法，打印参数、方法耗时
- 校验网络，无网络不执行，`@com.tk.tdroiddemo.aop.annotation.CheckNetwork`，修饰方法

## Base Activity && Fragment

- ViewStub初始化、加载中、错误视图、网络异常占位图设置 **TODO**
- **RxJava**绑定生命周期
- EventBus事件总线

## 工具类

- App工具类 utils.AppUtils
- 位操作工具类 utils.BitUtils
- 粘贴板工具类 utils.ClipboardUtils
- 集合处理工具类 utils.CollectionUtils
- 像素转换工具类 utils.DensityUtil
- 判空工具类 utils.EmptyUtils
- 文件IO流操作工具类 utils.FileIOUtils
- 文件工具类 utils.FileUtils
- Fragment切换帮助类,解决重叠问题 utils.FragmentHelper
- 图像压缩工具类 utils.ImageCompressUtils
- 图像工具类 utils.ImageUtils
- 常见意图工具类 utils.IntentUtils
- IO工具类 utils.IOUtils
- 日志工具类 utils.Logger
- 网络环境观察者 utils.NetworkObservable
- 网络环境观察者 **By RxJava** utils.NetworkRxObservable
- 反射工具类 utils.ReflectUtils
- 截屏工具类 utils.ScreenShotUtils
- 屏幕工具类 utils.ScreenUtils
- SharedPreference工具类 utils.SharedPreferenceUtils
- Shell命令工具类 utils.ShellUtils
- 大小转换工具类 utils.SizeUtils
- Spannable辅助类 utils.SpannableHelper
- 存储工具类 utils.StorageUtils
- Shell命令工具类 utils.ShellUtils
- 主题的配置工具类 utils.ThemeUtils
- 时间转换工具类 utils.TimeUtils
- 着色工具类 utils.TintUtils
- Toast打印工具 utils.Toasty
- Url工具类 utils.UrlUtils 
- **综合工具类** utils.Utils
- View代理工具类 utils.ViewProxyUtils
- View工具类 utils.ViewUtils
- 压缩、解压文件工具类 utils.ZipUtils

## 控件

- 过滤指定格式的**AppCompatEditText** view.FilterEditText
- View视图加载器，支持空视图、错误视图、加载中视图、无网络视图的配置 view.viewloader
- RecyclerView的工具 view.recycler


