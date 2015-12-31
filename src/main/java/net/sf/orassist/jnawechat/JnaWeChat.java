package net.sf.orassist.jnawechat;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.win32.W32APIOptions;

import static net.sf.orassist.jnawechat.MyUser32.*;

import java.util.ArrayList;

public class JnaWeChat {
	
	/**
	 * find window path
	 */
	private String[][] windowClassNames = 
	//360 Browser WeChat Plugin
	{
		{"360se6_Frame", "微信网页版"},
		{"SeWnd", null},
		{"Chrome_WidgetWin_1", null},
		{"Chrome_WidgetWin_0", null},
		{"Chrome_RenderWidgetHostHWND", null},
	};

//	JavaFX WebView, not work
//	{
//		{"GlassWndClass-GlassWindowClass-2", "StockToolkit"},
//	}
	
//	Baidu Browser WeChat Plugin
//	{
//		{"Baidu_PopupWnd", "微信网页版"},
//		{"BiDuWidget", null},
//		{"Chrome_WidgetWin_0", null},
//		{"Chrome_RenderWidgetHostHWND", null},
//	}

//	Chrome
//	{
//		{"Chrome_WidgetWin_1", "微信网页版 - Google Chrome"},
//		{"Chrome_RenderWidgetHostHWND", "Chrome Legacy Window"},
//	}
	
//	WeChat PC
//	{
//		{"WeChatMainWndForPC",""}
//	}
	
	private final HWND hwnd;
	
	public static void main(String[] args) throws IllegalStateException {
		new JnaWeChat().send("微信发送测试");
	}

	/**
	 * Init, find the window of WeChat browser or plugin. 
	 * Make sure the browser or plugin is opened and scanned with mobile phone. 
	 * @throws IllegalStateException 
	 */
	public JnaWeChat() throws IllegalStateException {
		
		HWND[] hwndArr = findWindow(windowClassNames);
		
		if ((hwnd = hwndArr[hwndArr.length - 1]) == null) {
			throw new IllegalStateException("Cannot locate WeChat window/plugin.");
		}
	}
	
	/** send text to WeChat as a message */
	public void send(String text) {

		if (hwnd != null) {
			
			WPARAM wPARAM;
			LPARAM lPARAM;
			
			for (char c:text.toCharArray()) {
				wPARAM = new WPARAM(c);
				lPARAM = new LPARAM(0);
	
				INSTANCE.SendMessage(hwnd, WM_CHAR, wPARAM, lPARAM);
			}

			wPARAM = new WPARAM(VK_RETURN);
			lPARAM = new LPARAM(0);
			
			INSTANCE.SendMessage(hwnd, WM_KEYDOWN, wPARAM, lPARAM);
			INSTANCE.SendMessage(hwnd, WM_KEYUP, wPARAM, lPARAM);
		}
		
	}
	
}

interface MyUser32 extends User32 {

	MyUser32 INSTANCE = (MyUser32) Native.loadLibrary("user32", MyUser32.class, W32APIOptions.DEFAULT_OPTIONS);

	int VK_RETURN = 0x0D;
	
	public abstract HWND FindWindowEx(HWND hwndParent, HWND hwndhwndAfter, String lpszClass, String lpszWindow);
	public abstract LRESULT SendMessage(HWND hWnd, int msg, WPARAM wParam, LPARAM lParam);
	
	/**
	 * find window through the path of window class and title array 
	 * @param windowClassTitleArr The array of window class and title pair 
	 * <pre>
	 * new String[][] { 
	 *   {"Top Window Class", "Top Window Title"}, 
	 *   {"Child Window Class 1", "Child Window Title 2"} 
	 *   ...
	 * }
	 * </pre>
	 * @return array of HWND which is one by one mapped from windowClassTitleArr 
	 */
	public static HWND[] findWindow(String[][] windowClassTitleArr) {
		ArrayList<HWND> hwndList = new ArrayList<HWND>(); 
		HWND hwnd;
		hwndList.add(hwnd = INSTANCE.FindWindow(windowClassTitleArr[0][0], windowClassTitleArr[0][1]));
		for (int i = 1; hwnd != null && i < windowClassTitleArr.length; i ++) {
			hwndList.add(hwnd = INSTANCE.FindWindowEx(hwnd, null, windowClassTitleArr[i][0], windowClassTitleArr[i][1]));
		}
		return hwndList.toArray(new HWND[windowClassTitleArr.length]);
	}
}
