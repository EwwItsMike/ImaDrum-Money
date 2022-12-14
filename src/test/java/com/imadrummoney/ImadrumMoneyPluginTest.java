package com.imadrummoney;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ImadrumMoneyPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(ImadrumMoneyPlugin.class);
		RuneLite.main(args);
	}
}