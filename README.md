

# OpenJDK FontConfiguation 空指针问题解决

该错误出现的原因是OpenJDK没有使用SXSSFWorkbook对应的支持的字体，可以通过添加扩充字体库解决该问题,以下为抛出的异常：



```aidl
Caused by: java.lang.NullPointerException
	at sun.awt.FontConfiguration.getVersion(FontConfiguration.java:1264)
	at sun.awt.FontConfiguration.readFontConfigFile(FontConfiguration.java:219)
	at sun.awt.FontConfiguration.init(FontConfiguration.java:107)
	at sun.awt.X11FontManager.createFontConfiguration(X11FontManager.java:774)
	at sun.font.SunFontManager$2.run(SunFontManager.java:431)
	at java.security.AccessController.doPrivileged(Native Method)
	at sun.font.SunFontManager.<init>(SunFontManager.java:376)
	at sun.awt.FcFontManager.<init>(FcFontManager.java:35)
	at sun.awt.X11FontManager.<init>(X11FontManager.java:57)
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62)
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)
	at java.lang.reflect.Constructor.newInstance(Constructor.java:423)
	at java.lang.Class.newInstance(Class.java:442)
	at sun.font.FontManagerFactory$1.run(FontManagerFactory.java:83)
	at java.security.AccessController.doPrivileged(Native Method)
	at sun.font.FontManagerFactory.getInstance(FontManagerFactory.java:74)
	at java.awt.Font.getFont2D(Font.java:491)
	at java.awt.Font.canDisplayUpTo(Font.java:2060)
	at java.awt.font.TextLayout.singleFont(TextLayout.java:470)
	at java.awt.font.TextLayout.<init>(TextLayout.java:531)
	at org.apache.poi.ss.util.SheetUtil.getDefaultCharWidth(SheetUtil.java:254)
	at org.apache.poi.xssf.streaming.AutoSizeColumnTracker.<init>(AutoSizeColumnTracker.java:117)
	at org.apache.poi.xssf.streaming.SXSSFSheet.<init>(SXSSFSheet.java:77)
	at org.apache.poi.xssf.streaming.SXSSFWorkbook.createAndRegisterSXSSFSheet(SXSSFWorkbook.java:636)
	at org.apache.poi.xssf.streaming.SXSSFWorkbook.createSheet(SXSSFWorkbook.java:629)
	at org.apache.poi.xssf.streaming.SXSSFWorkbook.createSheet(SXSSFWorkbook.java:71)
	at com.xfn.mf.utils.ExcelUtils1.exportExcel(ExcelUtils1.java:85)
	at com.xfn.mf.service.xfnImpl.ExcelServiceImpl.exportAssSubOne(ExcelServiceImpl.java:1492)
	at com.xfn.mf.service.xfnImpl.ExcelServiceImpl.lambda$degradeExportAssSubOne$9(ExcelServiceImpl.java:1357)
	at java.util.concurrent.FutureTask.run(FutureTask.java:266)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	... 1 more
```

```
yum -y install fontconfig
在 /usr/share 下多出 fontconfig 和 fonts 目录。
yum -y install zstd
下载字体 ttf-dejavu： Package: mingw-w64-x86_64-ttf-dejavu - MSYS2 Packages
tar -I zstd -xvf mingw-w64-x86_64-ttf-dejavu-2.37-3-any.pkg.tar.zst
cp mingw64/share/fonts/TTF/* /usr/share/fonts/
fc-cache --force
fc-list
重启服务进程
```