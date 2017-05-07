在 iOS 的设置里面，有一种编辑的效果，进入编辑状态后，列表左边推出圆形的删除按钮，点击后再出现右边确认删除按钮，相当于给用户二次确认。看下在 Android 上如何实现。

iOS 的效果如下：

![](http://upload-images.jianshu.io/upload_images/1763614-ba9eb0c50d80cd94.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![](http://upload-images.jianshu.io/upload_images/1763614-3dd5e1d0435fec7a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

我实现的效果是这样的：

![](http://upload-images.jianshu.io/upload_images/1763614-9e42d42ca3ebd6bb.gif?imageMogr2/auto-orient/strip)

下面说说我是怎么做的吧。

# EditLayout

我们自定义了一个 EditLayout 继承 FrameLayout。  
可以看出，这个控件由左中右三部分组成，对应的，我在 EditLsyout 里创建了以下成员变量：

```java
private View mContentView;  //内容部分
private View mLeftView;     //左边圆形删除按键
private View mRightView;    //右边删除按键
private int mWidth;         //内容部分宽度
private int mHeight;        //内容部分高度
private int mLeftWidth;     //左边部分宽度
private int mRightWidth;    //右边部分宽度
```

## 获取控件及宽高

当 View 中所有的子控件 均被映射成 xml 后，会触发 onFinishInflate 方法，当 view 的大小发生变化时，会触发 onSizeChanged 方法，所以我们可以这样赋值:

```java
@Override
protected void onFinishInflate() {
    super.onFinishInflate();
    mLeftView = getChildAt(0);
    mContentView = getChildAt(1);
    mRightView = getChildAt(2);
}

@Override
protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    mWidth = w;
    mHeight = h;
    mRightWidth = mRightView.getMeasuredWidth();
    mLeftWidth = mLeftView.getMeasuredWidth();
}
```

## 摆放控件位置

获取到控件和宽高，我们就可以摆放它们的位置了。我们知道，View 是通过 onLayout 方法来摆放控件位置的。这里有两种摆放方式，编辑状态和非编辑状态，代码如下：

```java
@Override
protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    //判断是否为编辑模式,摆放每个子View的位置
    if (EditAdapter.isEdit) {
        mContentView.layout(mLeftWidth, 0, mLeftWidth + mWidth, mHeight);
        mRightView.layout(mWidth + mLeftWidth, 0, mRightWidth + mWidth + LeftWidth, mHeight);
        mLeftView.layout(0, 0, mLeftWidth, mHeight);
    } else {
        mContentView.layout(0, 0, mWidth, mHeight);
        mRightView.layout(mWidth, 0, mRightWidth + mWidth, mHeight);
        mLeftView.layout(-mLeftWidth, 0, 0, mHeight);
    }
}
```

## 滑动效果

滑动效果，我交给了 ViewDragHelper 处理。要使用 ViewDragHelper ，需要实现一个 ViewDragHelper.Callback，这是一个抽象类，我们这里只关注它的三个方法：

```java
//返回值决定 child 是否可拖拽
public boolean tryCaptureView(View child, int pointerId)
//限定移动范围，返回值为对应控件的左边位置
public int clampViewPositionHorizontal(View child, int left, int dx)
//当 changedView 发生移动时的回调（可以用来更新其他子 View 的位置）
public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy)
```


我实现的 Callback 代码如下：

```java
ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {

    @Override
    public boolean tryCaptureView(View child, int pointerId) {
        return false;
    }

    @Override
    public int clampViewPositionHorizontal(View child, int left, int dx) {
        if (child == mContentView) {
            if (left < -mRightWidth) {
                left = -mRightWidth;
            } else if (left > mLeftWidth) {
                left = mLeftWidth;
            }
        } else if (child == mRightView) {
            if (left < mWidth - mRightWidth) {
                left = mWidth - mRightWidth;
            } else if (left > mWidth) {
                left = mWidth;
            }
        } else if (child == mLeftView) {
            if (left < mWidth - mRightWidth) {
                left = mWidth - mRightWidth;
            } else if (left > -mLeftWidth) {
                left = 0 - mLeftWidth;
            }
        }
        return left;
    }

    @Override
    public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
        if (changedView == mContentView) {
            mRightView.offsetLeftAndRight(dx);
            mLeftView.offsetLeftAndRight(dx);
        } else if (changedView == mRightView) {
            mContentView.offsetLeftAndRight(dx);
            mLeftView.offsetLeftAndRight(dx);
        } else if (changedView == mLeftView) {
            mContentView.offsetLeftAndRight(dx);
            mRightView.offsetLeftAndRight(dx);
        }
        invalidate();
    }
};

mDragHelper = ViewDragHelper.create(this, mCallback);
```

对了，实现滑动还需要重写 computeScroll 方法：

```java
@Override
public void computeScroll() {
    super.computeScroll();
    if (mDragHelper.continueSettling(true)) {
        ViewCompat.postInvalidateOnAnimation(this);
    }
}
```

## 三种状态

我们这个控件存在三种状态，分别是左边展开，右边展开，还有关闭。相应的，我们定义三个方法，用于滑动到不同的状态：

```java
/**
 * 展开左侧
 */
public void openLeft() {
    if (mOnStateChangeListener != null) {
        mOnStateChangeListener.onLeftOpen(this);
    }
    mDragHelper.smoothSlideViewTo(mContentView, mLeftWidth, 0);
    invalidate();
}

/**
 * 展开右侧
 */
public void openRight() {
    if (mOnStateChangeListener != null) {
        mOnStateChangeListener.onRightOpen(this);
    }
    mDragHelper.smoothSlideViewTo(mContentView, -mRightWidth, 0);
    invalidate();
}

/**
 * 关闭
 */
public void close() {
    if (mOnStateChangeListener != null) {
        mOnStateChangeListener.onClose(this);
    }
    mDragHelper.smoothSlideViewTo(mContentView, 0, 0);
    invalidate();
}
```

mOnStateChangeListener 是一个监听器，会在 EditLayout 状态改变的时候调用。我在回调方法里保存了当前向右展开的 EditLayout。

到这里，EditLayout 就完成了。

# EditAdapter

接下来看下适配器 EditAdapter。

## item 布局

item 的 xml 文件里面，最外层用我们的 EditLayout 包裹，然后里面的三个子布局，按顺序，对应我们左中右三个部分。

## 切换编辑模式

这里需要定义一个 EditLayout 的集合 allItems，在 onBindViewHolder 的时候将布局添加进去。  
然后我们定义两个公开方法，用于切换所有 item 的状态，在切换编辑模式的时候调用：

```java
/**
 * 关闭所有 item
 */
public void closeAll() {
    for (EditLayout layout : allItems) {
        editLayout.close();
    }
}

/**
 * 将所有 item 向左展开
 */
public void openLeftAll() {
    for (EditLayout layout : allItems) {
        editLayout.openLeft();
    }
}
```

# EditRecyclerView

当列表有某一项是右边展开了，我希望在滑动列表的时候能将它关闭，变回向左展开的状态，所以我自定义了一个 RecyclerView。

可以重写了 onTouchEvent 方法，实现上面说的效果：

```java
@Override
public boolean onTouchEvent(MotionEvent e) {
    switch (e.getAction()) {
        case MotionEvent.ACTION_DOWN:
            if (getAdapter() instanceof EditAdapter) {
                rightOpenItem = ((EditAdapter) getAdapter()).getRightOpenItem();
            }
            if (EditAdapter.isEdit && rightOpenItem != null) {
                rightOpenItem.openLeft();
            }
    }
    return super.onTouchEvent(e);
}
```

当滑动列表的时候，先判断是否有向右展开项，有的话就将它变回向左展开。

这样就完成啦，妥妥的。