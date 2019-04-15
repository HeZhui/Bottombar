package com.example.a15927.bottombardemo;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.a15927.bottombardemo.fragment.HomeFragment;
import com.example.a15927.bottombardemo.fragment.MeFragment;
import com.example.a15927.bottombardemo.fragment.SortFragment;
import com.example.a15927.bottombardemo.fragment.FindFragment;

public class MainActivity extends FragmentActivity implements BottomNavigationBar.OnTabSelectedListener{

    //本项目中需要使用到Google推出的BottomBar：BottomNavigationBar，所以这里先new一个对象mBottomNavigationBar。
    private BottomNavigationBar mBottomNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //1. 兵分两路，一部分是界面最底部的BottomBar初始化，一部分是随控的上方的界面Fragment的初始化

        //2. 本项目中需要使用的效果是这样的：
        //用户打开App，来到主界面，然后分别点击界面下方BottomBar的四个选项时，界面要切换到响应的Fragment

        //3. 设计匹配关系如下：
        //BottomBar       Fragment
        //home            HomeFragment
        //sort            SortFragment
        //find            FindFragment
        //me              MeFragment

        //4. 明确关系之后，我们就可以展开设计，但是点击事件归点击事件，但是刚进入App时，我们并没有点击BottomBar，那么这个时候界面上
        //没有任何的效果可行么？显然不行的，但是稍加思索，一开始进来的时候肯定是要显示第一个的界面的对吧，也就是我们设置默认的界面
        //为HomeFragment，所以onCreate（）方法中，需要定义如下方法setDefaultFragment（），也就是设置默认Fragment
        setDefaultFragment();

        //9. 视线再回来这里，上面Fragment的初始化已经弄好了，那BottomBar怎么着也都要设置一个默认的状态吧，也就是默认也要给一个状态，就是谁是被选中的状态
        //当然这个选中是假的，正如上面的Fragment也是个设置的默认值一样，那么我们让谁默认选中呢？第二个、第三个？显然不是的，毕竟
        //上面默认的Fragment都是第一个的HomeFragment，所以我们这里也需要设置第一个子项被选中
        //但是我们所说的第一个，对应的这个子项集合的下标可不是从1开始的，它是从0开始的，所以，可以往下看一下有一句话叫做setFirstSelectedPosition(0)
        //也就刚好顺了我们的意了
        //那么除了那一句话之外剩下的部分都是干什么的呢？
        //一看都是mBottomNavigationBar.set...那么自然可以想到这是对BottomBar的设置，想想也是自然的，BottomBar再神通广大，它也不过是一个通用的框架
        //别人用的跟我用的如果显示的文字或者是图片、背景等等，如果都是一样的，这样未免不合理吧，所以肯定要针对自己的项目做一定的配置嘛。
        //好，这个如果可以想通，那么就一句句看看这些都干了什么

        //10. 这一句不用说，控件绑定实例化，一开始虽然new了一个BottomNavigationBar的对象，但是到底是要来对UI控制的，所以一定要说明一下mBottomNavigationBar
        //是个什么东西，那么 mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar)，哎，一下子就明白了
        //这个mBottomNavigationBar所代表的就是一个id叫做bottom_navigation_bar的控件的对象，如果下面对mBottomNavigationBar有什么配置或操作的话
        //就相当于是对d叫做bottom_navigation_bar的控件的控制了，确认无误，接下来继续走
        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

        //11. 这部分开始就是真正的做配置了
        //第一个设置模式，都有什么模式供我们选择呢？
        //不懂的东西就百度一下，总有自己想要的答案，百度内容如下：
//        Mode包含3种Mode:
//        MODE_DEFAULT
//        如果Item的个数<=3就会使用MODE_FIXED模式，否则使用MODE_SHIFTING模式MODE_FIXED
//        填充模式，未选中的Item会显示文字，没有换挡动画。MODE_SHIFTING
//        换挡模式，未选中的Item不会显示文字，选中的会显示文字。在切换的时候会有一个像换挡的动画
        //那么我们这里就是一个填充模式了
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);

        //12. 这个字面上理解就是设置背景类型，它有什么供我们选择的呢？
//        Background Style包含3种Style:
//        BACKGROUND_STYLE_DEFAULT 如果设置的Mode为MODE_FIXED，将使用BACKGROUND_STYLE_STATIC 。如果Mode为MODE_SHIFTING将使用BACKGROUND_STYLE_RIPPLE。
//        BACKGROUND_STYLE_STATIC 点击的时候没有水波纹效果
//        BACKGROUND_STYLE_RIPPLE  点击的时候有水波纹效果
        //那么我们这里设置的是一个没有水波纹的效果的那种，当然我们也可以改成有水波纹的效果的那种，或许会更好看一些，但是毕竟任何App都有一个使用场合
        //还有一个喜好问题，如果不喜欢那么花里胡哨的，自然就是简单大方的更加得体一些了
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);

        //13. 这个设置简单明了，设置背景颜色，没啥好解释的，我们这里用的白色背景
        mBottomNavigationBar.setBarBackgroundColor(R.color.white);
        //14. 这个也很明了，就是未激活状态的颜色，说白了也就是如果一个子项没有被点击，它里面的文字颜色是什么呢？我们这里定义的是灰色
        mBottomNavigationBar.setInActiveColor(R.color.gray);

        //15. 这部分就是最关键的部分了，但是这部分并不难，同样很好理解。
        //这部分就是向BottomBar中添加一些子项，也就是你的BottomBar中究竟有什么选项可以被点击的呢？
        //看一看我们这里，一共有四个，分别是home、sort、find、me，那是自然，这是我们一开始就设计好了的
        //然后分别还要设置上对应的图片、还有文字、以及被点击之后的文字颜色变化，这里其实很多的属性都是自己可以选择要不要的，比如说你还可以为某一个item设置
        //一个角标budge，但是我这里没有用，所以就不添笔墨了，感兴趣的可以百度相关的内容
        mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.home,"Home").setActiveColorResource(R.color.homered))
                .addItem(new BottomNavigationItem(R.drawable.sort, "Sort").setActiveColorResource(R.color.sortgreen))
                .addItem(new BottomNavigationItem(R.drawable.find, "Find").setActiveColorResource(R.color.findblue))
                .addItem(new BottomNavigationItem(R.drawable.me, "Me").setActiveColorResource(R.color.mepink))
                //这句话就是我们上面说过了的，默认设置的是第一个被选中状态，下标从零开始，那么就是setFirstSelectedPosition(0)了，合情合理
                .setFirstSelectedPosition(0)
                //然后都设置好之后，就预置一下，然后用户看到的就是我们所设计的模样了。
                .initialise();

        //16. 同时呢，上面不过是设计了Fragment以及BottomNavigationBar的默认状态，但是我们更多的会进行一些点击，也就会更改子项的选中状态
        //我们可能会点home看看首页，点me看看个人信息，那么这部分是不确定的，所以就要做一定的监听，就像我们所熟知的按钮的点击事件一样
        //我们知道按钮点击事件有三种写法，那么这个BottomNavigationBar的选中监听自然也有不止一种写法，像这里，就是让Activity
        // 实现BottomNavigationBar.OnTabSelectedListener接口，然后onCreate（）中注册监听，然后再在别的地方去实现监听的方法了。
        mBottomNavigationBar.setTabSelectedListener(this);

    }

    private void setDefaultFragment() {
        //5. 视线转移到这个方法中，这个方法很简单，四行，但是功能是很强大的
        //第一行，因为我们需要设置Fragment，但是Fragment并不像Activity那样的来去自由，它的一举一动都需要被控制，被谁控制呢？
        //显然需要用FragmentManager，也就是Fragment管理器，这就很说得通了吧。
        //（当然Activity的加载也没那么自由，也会被底层分发机制控制，但是相对Fragment，还是不言而喻的）
        FragmentManager fragmentManager = getSupportFragmentManager();
        //6. 这一句是这样的，FragmentManager想要控制Fragment，但是在Activity中还不是由它说了算，所以需要申请一个“事务”
        //但是Activity并没有理由拒绝，所以只要你申请就批准，准许你开始你的事务beginTransaction（），想干啥干啥吧!
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //7. 这句话是new了一个HomeFragment的对象，毕竟这个方法是需要控制HomeFragment的
        HomeFragment homeFragment = new HomeFragment();
        //8. 好的，事务已经下达：replace(R.id.ll_content, homeFragment)，就是把HomeFragment替换到一个id为ll_content的控件之中
        //那么这个ll_content又是何许人也呢？按住ctrl左键点击，就找到对应的控件了，也就是界面上部分的那一大块空白区域，嗯，这正是我们想要的
        //然后事务拟定好了的话，就直接提交（commit）。
        //然后这件事就愉快的结束了。
        transaction.replace(R.id.ll_content, homeFragment).commit();
    }

    @Override
    public void onTabSelected(int position) {

        //17. 实现的监听就是这里了,回想一下我们一开始的设计思路，当我们选中了不同的BottomBar子项之后会做什么事情呢？
        //显然，自然是继续切换上面的Fragment嘛，还记得第 5 部分怎么讲这个切换的么？不记得就往上看一眼，就不再赘述了。
        //一开始的话，先定义一个Fragment的管理器，然后这个管理器去申请“事务”
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //18. 但是呢，毕竟我们有四个选项，每个选项对应的是不同的Fragment切换，但是本质上如果选中了任何一个，其实做的事情和设置默认的Fragment做的
        //动作都是一样的，只不过是切换的Fragment不同而已，所以我们这里就需要判断一下，用户选中了哪一个Fragment。
        //如下，做了一个Switch case的判断结构，这个应该大家并不陌生，如果不记得的话，一定要复习一下java相关的内容

        switch (position) {
            //19. 之前我们说过了，这个子项的id也就是position是从0开始的，我们一共有四个选项，所以对应的也就是0，1，2，3
            case 0:
                //20. 当position为0的子项被选中时，也就是第一个子项被选中时，就是把HomeFragment加载进来，替换掉之前的，调用了replace方法就不用管之前的
                //Fragment是哪一个，都会被直接替换掉的，也比较简单
                HomeFragment homeFragment = new HomeFragment();
                transaction.replace(R.id.ll_content, homeFragment);
                break;
            case 1:
                //21. 当position为1的子项被选中时，也就是第二个子项被选中时，就是把SortFragment加载进来，替换掉之前的。
                SortFragment sortFragment = new SortFragment();
                transaction.replace(R.id.ll_content, sortFragment);
                break;
            case 2:
                //22. 当position为2的子项被选中时，也就是第三个子项被选中时，就是把FindFragment加载进来，替换掉之前的。
                FindFragment findFragment = new FindFragment();
                transaction.replace(R.id.ll_content, findFragment);
                break;
            case 3:
                //23. 当position为3的子项被选中时，也就是第四个子项被选中时，就是把MeFragment加载进来，替换掉之前的。
                MeFragment meFragment = new MeFragment();
                transaction.replace(R.id.ll_content, meFragment);
                break;
            default:
                //24. 这个部分呢，因为用户要么就点击上面四个中的一个，要么就不点击，所以不会走到这里，所以就不定义放在这里就ok了
                break;
        }
        //25. 通过上面的条件判断呢，不管用户选择的是哪一个，都已经被捕获了，然后剩下的，直接把这件“事务”提交（commit）就好了。
        transaction.commit();

        //26. 至此，基本上整个的流程就走通了，然后大家就可以在这个结构的基础之上把自己的代码做进一步的完善了，MainActivity中定义了这样的一个结构，
        //但是用户可见的大部分都是四个Fragment中的其中一个，也就是如果我们需要定义什么布局，直接去相对应的Fragment中去定义就好，但是这并不意味着
        //MainActivity就可以高枕无忧了，前面也说过，Fragment并没有Activity自由，而且也并没有Activity那样的“权利”，所以，后面在Fragment中所定义的
        //任何一件和用户打交道的事情，都需要亮出Activity的名号才可以在App中自由行走，本质上也不过是奉命行事。当然这一切Activity并不需要直接参与，
        //但是Fragment中做的任何一件事，在MainActivity中都可以直接管控到，这部分现在大家有个了解就好，就是大概的了解一下Activity和Fragment的关系
        //形象的一个比喻就是你去租房子时你和房东的关系，虽然可能部分特性并不包含在这个比喻之中，不过单纯去理解这层关系的话，大差不差了。

    }

    //27 .剩下这两个回调方法，分别是用户没有选择和重复选择时需要做的事，但是我们这里并没有打算对这两种情景进行限制或设计
    //所以直接放在这里就好，但是并不能删除，毕竟我们实现了BottomNavigationBar.OnTabSelectedListener这个接口，所以我们就必须要
    //重写里面所有的方法，否则就会报错，如果不理解的话，还是要再复习一下java相关内容咯
    @Override
    public void onTabUnselected(int position) {
    }

    @Override
    public void onTabReselected(int position) {
    }

}
