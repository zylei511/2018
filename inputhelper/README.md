#inputhelpers使用文档
inputhelpers是聊天页面的输入框、表情、语音等控件的集合，主要用于对这些控件的控制。
所有功能的调用都是通过InputStateManager.class来实现，目前支持两种状态：输入和按钮，后面会详细讲。

##1.初始化
	InputStateManager inputStateManager = InputStateManager.newInstance();
	
##2.设置底部状态
	inputStateManager.setInputState(InputState);
其中InputState中有两种状态：STATE_INPUT和STATE_BOTTOM_BTN

| 状态                |解释                |
| -------------------|:------------------:| 
| STATE_INPUT        |输入状态，也是默认状态 |
| STATE_BOTTOM_BTN   |按钮状态             |

##3.设置需要放置的layout
	inputStateManager.setLayoutId(Context, int);

参数释义：

| 参数                |解释                    |
| -------------------|:----------------------:| 
| Context            |上下文                   |
| int                |需要填充的FramLayout的Id  |
	
##4.设置表情
只有在setInputState(InputState)的状态设置为InputState.STATE_INPUT才需要，为可选项

	inputStateManager.setFaceClasses(Class[]);
	
参数释义：

| 参数                |解释                                  |
| -------------------|:------------------------------------:| 
| Class[]            |表情类名，必须是BaseFaceEntity的子类      |

	
##5.设置更多
只有在setInputState(InputState)的状态设置为InputState.STATE_INPUT才需要，为可选项

	inputStateManager.setAddMore(ArrayList, ArrayList);
	
参数释义：

| 参数                          |解释                    |
| -----------------------------|:----------------------:| 
| ArrayList<String>            |名字                     |
| ArrayList<Integer>           |资源Id                   |
	
##6.设置是否有语音功能
只有在setInputState(InputState)的状态设置为InputState.STATE_INPUT才需要，为可选项

	inputStateManager.setHasAudio(boolean);
	
参数释义：

| 参数                |解释                                 |
| -------------------|:-----------------------------------:| 
| boolean            |false，没有语音按钮；true，有语音按钮     |

	
##7.设置底部按钮监听
只有在setInputState(InputState)的状态设置为InputState.STATE_BOTTOM_BTN才需要，为可选项

	inputStateManager.setBottomBtnClickListener(View.OnClickListener);
	
##8.设置显示
无论setInputState(InputState)的状态是InputState.STATE_INPUT还是	InputState.STATE_BOTTOM_BTN都需要调用这个方法，才能显示出来
	
	inputStateManager.show();

##9.改变按钮的状态
	inputStateManager.setBottom(String, String, int);
	
参数释义：

| 参数               |解释                     |
| ------------------|:----------------------:| 
| String            |Button上的文字            |
| String            |TextView顶部的提示        |
| int               |Button的颜色             |

##10.设置xml(推荐)
在聊天页面，当键盘隐藏和消失时会出现闪动的问题，为了解决这个问题，可以在页面的xml布局中如下设置：
###10.1页面根布局的设置
添加KPSwitchRootLinearLayout、KPSwitchRootFrameLayout或KPSwitchRootRelativeLayout到页面根目录中

例如：

	<com.paic.crm.inputhelper.kpswitch.widget.KPSwitchRootLinearLayout 
			xmlns:android="http://schemas.android.com/apk/res/android"
              android:id="@+id/rootView"
              android:layout_width="match_parent"
              android:layout_height="match_parent"
              android:orientation="vertical">
              
              ...
              
	</com.paic.crm.inputhelper.kpswitch.widget.KPSwitchRootLinearLayout>

##11.监听

如果想要实现相应的功能，除了上面的那些内容，还需要实现下面相应的接口：

|方法                      |解释               |方法                     |参数
| ----------------------- |:----------------:|:-----------------------:|:---
|setOnRecordFinishListener|录制语音结束的回调   |onRecordFinish(String)   |语音文件的地址
|setOnMoreItemClickListenr       |【更多】里面的item点击事件|onMoreItemClick(AdapterView<?> parent, View view, int position, long id)|和OnItemClick参数一样
|setOnSendClickListener           |发送按钮点击回调接口 |onSendBtnClick(String content)|发送的内容
|setBottomBtnClickListener| STATEBOTTOMBTN状态下的点击事件|onClick(View v)

##12.关闭键盘和底部panel

这个是可以选择的，点击屏幕，关闭整个panel和键盘：

	InputStateManager.getInstance().hidePanelAndKeybroad();