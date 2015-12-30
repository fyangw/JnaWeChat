package net.sf.orassist.jnawechat;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.win32.W32APIOptions;

import static net.sf.orassist.jnawechat.MyUser32.*;

interface MyUser32 extends User32 {

	MyUser32 INSTANCE = (MyUser32) Native.loadLibrary("user32", MyUser32.class, W32APIOptions.DEFAULT_OPTIONS);

	int VK_A = 0x61;
	int VK_RETURN = 0x0D;
	int VK_ENTER = 0x0D;
	
	public abstract HWND FindWindowEx(HWND hwndParent, HWND hwndhwndAfter, String lpszClass, String lpszWindow);
	public abstract LRESULT SendMessage(HWND hWnd, int msg, WPARAM wParam, LPARAM lParam);
	
}

public class JnaWeChat {
	
	public static void main(String[] args) {
		
		HWND hwnd = INSTANCE.FindWindow("WeChatMainWndForPC", null);
		System.out.println("hwnd " + (hwnd == null ? hwnd : hwnd.getPointer()));
		
		if (hwnd != null) {
			
			System.out.println("hwnd " + (hwnd == null ? hwnd : hwnd.getPointer()));
			
//			INSTANCE.ShowWindow(hwnd, SW_RESTORE);
			INSTANCE.SetForegroundWindow(hwnd);
	
			INSTANCE.SetFocus(hwnd);
			
			WPARAM wPARAM = new WPARAM(VK_A);
			LPARAM lPARAM = new LPARAM(0);

			//MyUser32.INSTANCE.PostMessage(hwnd, WM_KEYDOWN, wPARAM, lPARAM);
			INSTANCE.SendMessage(hwnd, WM_KEYDOWN, wPARAM, lPARAM);
			INSTANCE.SendMessage(hwnd, WM_CHAR, wPARAM, lPARAM);
			INSTANCE.SendMessage(hwnd, WM_KEYUP, wPARAM, lPARAM);

			wPARAM = new WPARAM(VK_ENTER);
			lPARAM = new LPARAM(0);
			
			//MyUser32.INSTANCE.PostMessage(hwnd, WM_KEYDOWN, wPARAM, lPARAM);
			INSTANCE.SendMessage(hwnd, WM_KEYDOWN, wPARAM, lPARAM);
			INSTANCE.SendMessage(hwnd, WM_CHAR, wPARAM, lPARAM);
			INSTANCE.SendMessage(hwnd, WM_KEYUP, wPARAM, lPARAM);
		}
	}
}
