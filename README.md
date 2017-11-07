[![](https://travis-ci.org/SouthernBox/EditMode.svg?branch=master)](https://travis-ci.org/SouthernBox/EditMode)
[![](https://api.bintray.com/packages/southernbox/maven/EditMode/images/download.svg)](https://bintray.com/southernbox/maven/EditMode/_latestVersion)
[![](https://img.shields.io/badge/Android%20Arsenal-EditMode-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/6415)
[![](https://img.shields.io/badge/API-15+-green.svg?style=flat)](https://android-arsenal.com/api?level=15)
[![](https://badge.juejin.im/entry/590e9c32a0bb9f00589648fb/likes.svg?style=flat)](https://juejin.im/post/590e9bb02f301e0057d366f1)
[![](https://badge.juejin.im/entry/5926aa5a44d90400640577a3/likes.svg?style=flat)](https://juejin.im/post/5926a980a0bb9f0057c718e7)

# EditMode

List of edit mode, including delete, sort function.

![](http://upload-images.jianshu.io/upload_images/1763614-d081e7328eceb664.gif?imageMogr2/auto-orient/strip)

# Usage

**Add the dependencies to your gradle file:**

```javascript
dependencies {
    compile 'com.southernbox:EditMode:1.0.0'
}
```
**In the activity layout file, replace the RecyclerView with EditRecyclerView:**

```xml
<com.southernbox.editmode.EditRecyclerView
    android:id="@+id/recycler_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

**Use EditLayout in the item layout file:**

```xml
<com.southernbox.editmode.EditLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/edit_layout"
    android:layout_width="match_parent"
    android:layout_height="@dimen/item_height">

    <!--Delete Button-->

    <!--Content-->

    <!--PreDelete button-->

    <!--Content-->

    <!--Sort Button-->

</com.southernbox.editmode.EditLayout>
```

![](http://upload-images.jianshu.io/upload_images/1763614-eec83eb5a49a164c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

**Extend EditAdapter:**

```java
public class MyAdapter extends EditAdapter<Entity> {

    MyAdapter(Context context, List<Entity> list) {
        super(context, list);
    }

    @Override
    public EditViewHolder onCreateEditViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindEditViewHolder(EditViewHolder holder, int position) {
        // bind holder.vContent
    }

    private static class ViewHolder extends EditViewHolder {
        // ...
    }
}
```
