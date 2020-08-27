package com.edgar.yurihome;

import android.util.Log;

public class TestJson {

    private static final String TAG = "=====================" + TestJson.class.getSimpleName();

    private String jsonString = "{\"commentIds\":[\"29973392\",\"29973344\",\"29973288\",\"29973277\",\"29973223\",\"29973186\",\"29973107,29972596\",\"29973028,29972728\",\"29972840\",\"29972839\",\"29972781\",\"29972728\",\"29972596\",\"29972575\",\"29972564\",\"29972484\",\"29972475\",\"29972457\",\"29972391\",\"29972195\"],\"comments\":{\"29973392\":{\"obj_id\":\"56325\",\"content\":\"\\u5bb3\\u6015\\uff0c\\u4e3a\\u4ec0\\u4e48\\u732b\\u4f1a\\u8bf4\\u8bdd\\uff1f\",\"sender_uid\":\"102405440\",\"sender_ip\":\"182.51.86.252\",\"create_time\":\"1598502051\",\"is_goods\":\"0\",\"like_amount\":\"1\",\"upload_images\":\"\",\"sender_terminal\":\"1\",\"origin_comment_id\":\"0\",\"id\":\"29973392\",\"avatar_url\":\"https:\\/\\/avatar.dmzj.com\\/46\\/44\\/464426121c2786a3dbb624b512be1bd8.png\",\"nickname\":\"S\\u603b\\u5947\\u9047\\u8bb0\",\"sex\":\"1\"},\"29973344\":{\"obj_id\":\"56325\",\"content\":\"\\u53ef\\u7231\",\"sender_uid\":\"100817759\",\"sender_ip\":\"222.169.177.78\",\"create_time\":\"1598501814\",\"is_goods\":\"0\",\"like_amount\":\"0\",\"upload_images\":\"\",\"sender_terminal\":\"1\",\"origin_comment_id\":\"0\",\"id\":\"29973344\",\"avatar_url\":\"https:\\/\\/avatar.dmzj.com\\/70\\/40\\/70404282c41e3e6c82a7219912efb3a0.png\",\"nickname\":\"\\u8086\\u98ce\",\"sex\":\"2\"},\"29973288\":{\"obj_id\":\"56325\",\"content\":\"\\u8fd9\\u4e2a\\u59b9\\u59b9\\u6027\\u683c\\u597d\\u9177\",\"sender_uid\":\"104673385\",\"sender_ip\":\"117.176.140.107\",\"create_time\":\"1598501579\",\"is_goods\":\"0\",\"like_amount\":\"0\",\"upload_images\":\"\",\"sender_terminal\":\"1\",\"origin_comment_id\":\"0\",\"id\":\"29973288\",\"avatar_url\":\"https:\\/\\/avatar.dmzj.com\\/70\\/33\\/70331a5071313642727910783d473a98.png\",\"nickname\":\"sumu\",\"sex\":\"1\"},\"29973277\":{\"obj_id\":\"56325\",\"content\":\"\\u597d\",\"sender_uid\":\"105337252\",\"sender_ip\":\"101.130.212.227\",\"create_time\":\"1598501541\",\"is_goods\":\"0\",\"like_amount\":\"0\",\"upload_images\":\"\",\"sender_terminal\":\"1\",\"origin_comment_id\":\"0\",\"id\":\"29973277\",\"avatar_url\":\"https:\\/\\/avatar.dmzj.com\\/96\\/46\\/9646a90258e935807d268583da068ec8.png\",\"nickname\":\"youak\",\"sex\":\"1\"},\"29973223\":{\"obj_id\":\"56325\",\"content\":\"\\u597d\\u53ef\\u7231\",\"sender_uid\":\"106305535\",\"sender_ip\":\"42.234.28.89\",\"create_time\":\"1598501255\",\"is_goods\":\"0\",\"like_amount\":\"0\",\"upload_images\":\"\",\"sender_terminal\":\"1\",\"origin_comment_id\":\"0\",\"id\":\"29973223\",\"avatar_url\":\"https:\\/\\/avatar.dmzj.com\\/31\\/4b\\/314b054b93655225e977d2dfdeef2a52.png\",\"nickname\":\"\\u72ac\\u66f0\\u4eca\\u5929\\u5929\\u6c14\\u4e0d\\u9519\",\"sex\":\"1\"}},\"total\":20}";

    public TestJson() {
        String result = jsonString.replace("\"comments\":{", "\"comments\":[");
        result = result.replace("},\"total\"", "],\"total\"");
        result = result.replaceAll("\"[0-9]*\":", "");
        Log.d(TAG, "result: " + result);
    }
}
