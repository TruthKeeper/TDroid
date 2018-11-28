# Plan

## 功能组件

### 事件传递 .bridge

- **EventManager**：事件通信管理器，用于发送一个事件（支持粘性）以供多个接收端接收，支持组件化跨模块场景下调用
- **ServiceManager**：服务注册工具，用于注册服务以供组件化场景下的跨模块调用

### Http

> Retrofit2 + OkHttp3 + RxJava2 + Gson 示例代码见 sample.SampleHttp

- 动态BaseUrl http.RuntimeUrlManager
- Https的支持
- Cookie的管理 http.CookieManager
- 日志打印
- 离线缓存
- 全局监听请求、响应、Glide请求的进度，支持API中动态、静态配置 , 适配重定向 widget.http.progress.ProgressManager

### Image

- 图像加载器 .image.load.ImageLoader
- 图片(拍照、相册、裁剪)选择器 .image.selector.ImageSelector

### 6.0+权限动态获取 permission By RxJava2

- PermissionManager.with().request()
- PermissionManager.with().requestEach()

## APT 编译时生成注解

- **反射实例化工具**  `com.apt.<组件名>.InstanceFactory`，对支持APT的类添加注解`com.tdroid.annotation.Instance`
- **自动读取数据**  自动注入到 **Activity**(By getIntent) , **Fragment**(By getArguments)中 , 已集成在**BaseActivity**和**BaseFragment**中，
对Field修饰注解`com.tdroid.annotation.AutoInject` , 重写`autoInjectData()`返回true即可开启
- **自动保存数据**  用于在Activity 和 Fragment onSaveInstanceState时自动保存数据，并在合适时机还原数据 , 已集成在**BaseActivity**和**BaseFragment**中，
  对Field修饰注解`com.tdroid.annotation.SaveAndRestore` , 重写`saveAndRestoreData()`返回true即可开启
- **路由**  组件化跨模块跳转**Activity**和获取**Service、Fragment**，需要在App工程中注册路由表` TRouter.register(new com.apt.<组件名>.RouterTable());`

## Base Activity && Fragment

- ViewStub初始化、加载中、错误视图、网络异常占位图设置 **TODO**
- **RxJava**绑定生命周期
- EventBus事件总线
- 自动读取数据注入
- 自动保存、还原数据

### Dex

支持多进程加载Dex

## 工具类

- 音频焦点帮助类 utils.audio.AudioFocusHelper
- 声音播放帮助类 utils.audio.SoundPlayHelper
- 录制声音管理类 utils.audio.AudioRecordHelper
- 扬声器帮助工具 utils.audio.SpeakerHelper
- App工具类 utils.AppUtils
- Bar工具类 utils.BarUtils
- 位操作工具类 utils.BitUtils
- 粘贴板工具类 utils.ClipboardUtils
- 集合处理工具类 utils.CollectionUtils
- 转换工具类 utils.ConvertUtils
- 像素转换工具类 utils.DensityUtil
- Drawable工具类 utils.DrawableUtils
- 判空工具类 utils.EmptyUtils
- 文件IO流操作工具类 utils.FileIOUtils
- 文件工具类 utils.FileUtils
- App前后台切换观察者 By RxJava utils.ForegroundObservable
- Fragment切换帮助类,解决重叠问题 utils.FragmentHelper
- 图像压缩工具类 utils.ImageCompressUtils
- 图像工具类 utils.ImageUtils
- 常见意图工具类 utils.IntentUtils
- IO工具类 utils.IOUtils
- 日志工具类 utils.Logger
- 网络环境观察者 **By RxJava** utils.NetworkObservable
- 网络工具 utils.NetworkUtils
- 数值转换工具 utils.NumberUtils
- 反射工具类 utils.ReflectUtils
- 屏幕截屏监听可观察者 **By RxJava** utils.ScreenShotObservable
- 截屏工具类 utils.ScreenShotUtils
- 屏幕工具类 utils.ScreenUtils
- SelectorDrawable生成工具 utils.SelectorFactory
- SharedPreference工具类 utils.SharedPreferenceUtils
- Shell命令工具类 utils.ShellUtils
- 大小转换工具类 utils.SizeUtils
- 软键盘观察者 utils.SoftKeyBoardObservable
- 软键盘工具类 utils.SoftKeyboardUtils
- Spannable生成工具 utils.SpannableFactory
- 存储工具类 utils.StorageUtils
- 主题的配置工具类 utils.ThemeUtils
- 时间转换工具类 utils.TimeUtils
- Toast打印工具 utils.Toasty
- Url工具类 utils.UrlUtils 
- **综合工具类** utils.Utils
- View代理工具类 utils.ViewProxyUtils
- View工具类 utils.ViewUtils
- 压缩、解压文件工具类 utils.ZipUtils

## .result

优雅的开启Activity回调 , 不需要重写回调方法

## .ui

封装UI的底层控件

## .viewloader

View视图加载器，支持空视图、错误视图、加载中视图、无网络视图的配置

## .recycler

RecyclerView的扩展

## .view

- **CropLayout** 包裹裁剪容器，支持Stroke 
- **FilterEditText** 过滤指定格式的**AppCompatEditText**
- **IndicatorView** Builder模式对View添加数字指示器