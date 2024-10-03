package com.pasc.lib.keyboard;

/**
 * 键盘输入回调坚挺
 * @author yangzijian
 * @date 2019/3/10
 * @des
 * @modify
 **/
public interface PwdKeyBoardListener {
    /**
     * 密码输入完成输入完成（已到达最大密码输入位数）
     * @param password 密码
     * @param isInvalidatePwd 是否为简单密码
     */
    void onPasswordInputFinish(String password, boolean isInvalidatePwd);

    /**
     *
     * @param currentIndex
     * @param totalLength
     */
    void addPassWord(int currentIndex, int totalLength);

    void removePassWord(int currentIndex, int totalLength);

    void clearPassWord(int currentIndex, int totalLength);
}
