# 此为模仿QQ音乐歌词弹幕效果Demo

使用系统的LinearLayout，及系统的容器动画特性，就可以模仿出QQ音乐歌词界面那个炫酷的弹幕显示控件（ps:不知道QQ是不是一样的实现）。
设置好LinearLayout的gravity属性为bottom后，再指定其setLayoutTransition为自定义的动画组合。
就可以实现新增子控件缩放显示，删除子控件，缩放退出效果。
