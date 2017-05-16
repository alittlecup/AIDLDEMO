# AIDLDEMO
一个简单的AIDL的Demo,相对于其他的AIDL的Demo多的是自己手写的一个AIDL,不用系统帮着实现的。
先说简单的AIDL的使用方式:
1. 利用系统的AIDL生成一个AIDL文件，然后在其中写接口。
2. build项目之后就会在build->generated->source->aidl中生成系统提供的aidl文件。
3. 在service中创建一个实现类，然后返回实现的IBinder对象。
客户端在使用的时候使用绑定模式绑定一个Service，然后在ServiceConnection的onServiceConnected中获得Proxy对象。

# 学到的点
1. aidl文件不是必须的，项目中自定义aidl的时候全都是使用的java代码，所以aidl不是必须的，只不过利用aidl系统能为我们自动生成代码（猜测）。
2. 生成的代码中，一部分代码并不是必须的，比如服务端的proxy，并且asInterface这个方法名也不是固定的，客户端与服务端也不要求一致。


