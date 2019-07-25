package com.example.a15927.bottombardemo.Utils;

/**
 * Created by Administrator on 2019/7/25.
 */

public class ThreeDesUtils {
//    private static final String Algorithm = "DESede";    //3DES算法
//
//    private static byte[] ivs = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 };
//    private static IvParameterSpec iv = new IvParameterSpec(ivs);
//    /************************************************************
//     Function:       // encryptMode(byte[] src,byte[] key)
//     Description:    // 3DES_ECB_EN
//     Input:          // src-源数据(byte[]) key-加密秘钥(byte[])
//     Output:         // 加密后的数据
//     Return:         // byte[]
//     *************************************************************/
//    public static byte[] encryptMode(byte[] src,byte[] key) {
//        try {
//            // System.out.println("没到8bytes:"+byteArrayToHexString(src));
//            SecretKey deskey = new SecretKeySpec(key, Algorithm);    //生成密钥21
//            Cipher c1 = Cipher.getInstance("DESede/ECB/NoPadding");    //实例化负责加密/解密的Cipher工具类22
//
//
//            c1.init(Cipher.ENCRYPT_MODE, deskey);    //初始化为加密模式23
//            return c1.doFinal(src);         } catch (java.security.NoSuchAlgorithmException e1) {
//            e1.printStackTrace();          } catch (javax.crypto.NoSuchPaddingException e2) {
//            e2.printStackTrace();          } catch (Exception e3) {
//            e3.printStackTrace();}
//        return null;      }
//    /************************************************************
//     Function:       // decryptMode(byte[] src,byte[] key)
//     Description:    // 3DES_ECB_DE
//     Input:          // src-源数据(byte[]) key-解密秘钥(byte[])
//     Output:         // 解密后的数据
//     Return:         // byte[]
//     *************************************************************/
//    public static byte[] decryptMode(byte[] src ,byte[] key) {
//        try {SecretKey deskey = new SecretKeySpec(key, Algorithm);
//            Cipher c1 = Cipher.getInstance("DESede/ECB/NoPadding");
//            c1.init(Cipher.DECRYPT_MODE, deskey);    //初始化为解密模式44
//            return c1.doFinal(src);         } catch (java.security.NoSuchAlgorithmException e1) {
//            e1.printStackTrace();         } catch (javax.crypto.NoSuchPaddingException e2) {
//            e2.printStackTrace();        } catch (Exception e3) {
//            e3.printStackTrace();         }         return null;      }
//
//    /************************************************************
//     Function:       // length_process(String src)
//     Description:    // 计算MAC时用到，字符串长度处理为8byte的倍数
//     Input:          // src-待变长的源数据(String)
//     Output:         // 变长后后的源数据
//     Return:         // String
//     *************************************************************/
//    private static String length_process(String src) {
//        if(src.length()%16==0){
//            return src;
//        }else if(src.length()%16==14){
//            return src+"80";
//        }else{
//            src+="80";
//            while(src.length()%8!=0){
//                src+="0";
//            }
//            return src;
//        }
//    }
//
//    /*
//         keyBytes：必须是16字节或者24字节，否则解析不了。
//     */
//    public static String DES_ECB_EN(String data,byte[] keyBytes){
//        String msg = length_process(data);
//        byte[] src =  hexStringToByteArray(msg);
//        byte[] secretStr = ThreeDesUtils.encryptMode(src,keyBytes);    //调用加密方法:16
//        return byteArrayToHexString(secretStr);
//    }
//
//    /*
//      keyBytes：必须是16字节或者24字节，否则解析不了。
//     */
//    public static String DES_ECB_DE(String secretStr,byte[] keyBytes){
//        byte[] myMsg = ThreeDesUtils.decryptMode(hexStringToByteArray(secretStr),keyBytes);  //调用解密方法17
//        return byteArrayToHexString(myMsg);
//    }
//
//
//    // 字节数组转换为 16 进制
//    public static String byteArrayToHexString(byte[] bytes) {
//        final String HEX = "0123456789ABCDEF";
//        StringBuilder sb = new StringBuilder(bytes.length * 2);
//        for (byte b : bytes) {
//            // 取出这个字节的高4位，然后与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数
//            sb.append(HEX.charAt((b >> 4) & 0x0f));
//            // 取出这个字节的低位，与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数
//            sb.append(HEX.charAt(b & 0x0f));
//        }
//
//        return sb.toString();
//    }
//
//    /**
//     * Convert hex string to byte[]
//     *
//     * @param hexString the hex string
//     * @return byte[]
//     */
//    public static byte[] hexStringToByteArray(String hexString) {
//        if (hexString == null || hexString.equals("")) {
//            return null;
//        }
//        hexString = hexString.toUpperCase();
//        int length = hexString.length() / 2;
//        char[] hexChars = hexString.toCharArray();
//        byte[] d = new byte[length];
//        for (int i = 0; i < length; i++) {
//            int pos = i * 2;
//            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
//        }
//        return d;
//    }
//
//
//    /**
//     * 字符串转换成十六进制字符串
//     * @param  str 待转换的ASCII字符串
//     * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
//     */
//    public static String str2HexStr(String str)
//    {
//
//        char[] chars = "0123456789ABCDEF".toCharArray();
//        StringBuilder sb = new StringBuilder("");
//        byte[] bs = str.getBytes();
//        int bit;
//
//        for (int i = 0; i < bs.length; i++)
//        {
//            bit = (bs[i] & 0x0f0) >> 4;
//            sb.append(chars[bit]);
//            bit = bs[i] & 0x0f;
//            sb.append(chars[bit]);
//            sb.append(' ');
//        }
//        return sb.toString().trim();
//    }
//
//    /**
//     * 16进制字符串转换为字符串
//     *
//     * @param s
//     * @return
//     */
//    public static String hexStringToString(String s) {
//        if (s == null || s.equals("")) {
//            return null;
//        }
//        s = s.replace(" ", "");
//        byte[] baKeyword = new byte[s.length() / 2];
//        for (int i = 0; i < baKeyword.length; i++) {
//            try {
//                baKeyword[i] = (byte) (0xff & Integer.parseInt(
//                        s.substring(i * 2, i * 2 + 2), 16));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        try {
//            s = new String(baKeyword, "gbk");
//            new String();
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
//        return s;
//    }
//
//
//    /**
//     * Convert char to byte
//     *
//     * @param c char
//     * @return byte
//     */
//    public static byte charToByte(char c) {
//        return (byte) "0123456789ABCDEF".indexOf(c);
//    }
//
//
//
//
//
//    //==========================================================================================================================
//    //
//    // 在原有的3Des基础上改变：
//    //    A.增加了对被加密数据长度的校验，必须是8的倍数
//    //    B. key必须是16的倍数 ，如果不是，则key = ( key + key + key ).substring(0, 48 )  //key = 16时
//    //                                     key = ( key + key ).substring(0, 48 )  //key = 32时
//    //                                     key = key.substring(0, 48 )  //key >= 48时
//    //==========================================================================================================================
//
//
//
//    /*
//     * 判断是否为16进制串
//     */
//    public static boolean checkIsHexString(String s) {
//        String regex = "^[A-Fa-f0-9]+$";
//        if (s.matches(regex)) {
//            System.out.println(s.toUpperCase() + " is Hex str");
//            return true;
//        } else {
//            System.out.println(s.toUpperCase() + " isn't Hex str");
//            return false;
//        }
//    }
//
//    /**
//     * Get kcv by hex str string.
//     *
//     * @param hexStr the hex str
//     * @return the string
//     */
///*
//        计算16进制串字符串的KCV
//    */
//    public static String getKCVByHexStr(String hexStr) {
//        if (hexStr == null) {
//            return null;
//        }
//
//        if (hexStr.length() == 16) {
//            hexStr = (hexStr + hexStr + hexStr).substring(0, 48);
//        }else if (hexStr.length() == 32) {
//            hexStr = (hexStr + hexStr).substring(0, 48);
//        }else if(hexStr.length() >= 48)	{
//            hexStr = hexStr.substring(0, 48);
//        }
//        return ThreeDesUtils.DES_ECB_EN("0000000000000000", ByteUtils.hexString2ByteArray(hexStr));
//    }
//
//
//    public static String getThreeDesEncrypt(String hexData,String hexKey) {
//        if (hexData==null || hexKey == null) {
//            return null;
//        }
//
//        if (hexKey.length() == 16) {
//            hexKey = (hexKey + hexKey + hexKey).substring(0, 48);
//        }else if (hexKey.length() == 32) {
//            hexKey = (hexKey + hexKey).substring(0, 48);
//        }else if(hexKey.length() >= 48)	{
//            hexKey = hexKey.substring(0, 48);
//        }
//        return ThreeDesUtils.DES_ECB_EN(hexData, ByteUtils.hexString2ByteArray(hexKey));
//    }
//
//    public static String getThreeDesDecrypt(String hexData,String hexKey) {
//        if (hexData==null || hexKey == null) {
//            return null;
//        }
//
//        if (hexKey.length() == 16) {
//            hexKey = (hexKey + hexKey + hexKey).substring(0, 48);
//        }else if (hexKey.length() == 32) {
//            hexKey = (hexKey + hexKey).substring(0, 48);
//        }else if(hexKey.length() >= 48)	{
//            hexKey = hexKey.substring(0, 48);
//        }
//        return ThreeDesUtils.DES_ECB_DE(hexData, ByteUtils.hexString2ByteArray(hexKey));
//    }
//
//    // 测试方法
//    public static void main(String args[]){
//
//        String key =  "9655371ba882b0af9655371ba882b0af";
//        String data = "a2345678123456781234567812345678";
//
//        String enStr = getThreeDesEncrypt(data,key);
//        String deStr = getThreeDesDecrypt(enStr,key);
//
//        System.out.println( enStr ) ;
//        System.out.println( deStr ) ;
//
//    }
}
