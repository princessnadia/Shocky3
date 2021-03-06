package shocky3;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.Listener;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.*;
import org.pircbotx.hooks.types.*;

public abstract class ListenerPlugin extends Plugin {
	public final Listener<PircBotX> listener;
	
	public ListenerPlugin(PluginInfo pinfo) {
		super(pinfo);
		listener = new MyListener(this);
	}
	
	void preOnLoad() {
		for (BotManager bm : botApp.serverManager.botManagers) {
			for (PircBotX bot : bm.bots) {
				bot.getConfiguration().getListenerManager().addListener(listener);
			}
		}
	}
	void preOnUnload() {
		for (BotManager bm : botApp.serverManager.botManagers) {
			for (PircBotX bot : bm.bots) {
				bot.getConfiguration().getListenerManager().removeListener(listener);
			}
		}
	}
	
	protected void onEvent(Event<PircBotX> e) {}
	protected void onAction(ActionEvent<PircBotX> e) {}
	protected void onChannelInfo(ChannelInfoEvent<PircBotX> e) {}
	protected void onConnect(ConnectEvent<PircBotX> e) {}
	protected void onDisconnect(DisconnectEvent<PircBotX> e) {}
	protected void onFinger(FingerEvent<PircBotX> e) {}
	protected void onGenericChannel(GenericChannelEvent<PircBotX> e) {}
	protected void onGenericChannelMode(GenericChannelModeEvent<PircBotX> e) {}
	protected void onGenericChannelUser(GenericChannelUserEvent<PircBotX> e) {}
	protected void onGenericCTCP(GenericCTCPEvent<PircBotX> e) {}
	protected void onGenericDCC(GenericDCCEvent<PircBotX> e) {}
	protected void onGenericMessage(GenericMessageEvent<PircBotX> e) {}
	protected void onGenericUser(GenericUserEvent<PircBotX> e) {}
	protected void onGenericUserMode(GenericUserModeEvent<PircBotX> e) {}
	protected void onHalfOp(HalfOpEvent<PircBotX> e) {}
	protected void onIncomingChatRequest(IncomingChatRequestEvent<PircBotX> e) {}
	protected void onIncomingFileTransfer(IncomingFileTransferEvent<PircBotX> e) {}
	protected void onInvite(InviteEvent<PircBotX> e) {}
	protected void onJoin(JoinEvent<PircBotX> e) {}
	protected void onKick(KickEvent<PircBotX> e) {}
	protected void onMessage(MessageEvent<PircBotX> e) {}
	protected void onMode(ModeEvent<PircBotX> e) {}
	protected void onMotd(MotdEvent<PircBotX> e) {}
	protected void onNickAlreadyInUse(NickAlreadyInUseEvent<PircBotX> e) {}
	protected void onNickChange(NickChangeEvent<PircBotX> e) {}
	protected void onNotice(NoticeEvent<PircBotX> e) {}
	protected void onOp(OpEvent<PircBotX> e) {}
	protected void onOwner(OwnerEvent<PircBotX> e) {}
	protected void onPart(PartEvent<PircBotX> e) {}
	protected void onPing(PingEvent<PircBotX> e) {}
	protected void onPrivateMessage(PrivateMessageEvent<PircBotX> e) {}
	protected void onQuit(QuitEvent<PircBotX> e) {}
	protected void onRemoveChannelBan(RemoveChannelBanEvent<PircBotX> e) {}
	protected void onRemoveChannelKey(RemoveChannelKeyEvent<PircBotX> e) {}
	protected void onRemoveChannelLimit(RemoveChannelLimitEvent<PircBotX> e) {}
	protected void onRemoveInviteOnly(RemoveInviteOnlyEvent<PircBotX> e) {}
	protected void onRemoveModerated(RemoveModeratedEvent<PircBotX> e) {}
	protected void onRemoveNoExternalMessages(RemoveNoExternalMessagesEvent<PircBotX> e) {}
	protected void onRemovePrivate(RemovePrivateEvent<PircBotX> e) {}
	protected void onRemoveSecret(RemoveSecretEvent<PircBotX> e) {}
	protected void onRemoveTopicProtection(RemoveTopicProtectionEvent<PircBotX> e) {}
	protected void onServerPing(ServerPingEvent<PircBotX> e) {}
	protected void onServerResponse(ServerResponseEvent<PircBotX> e) {}
	protected void onSetChannelBan(SetChannelBanEvent<PircBotX> e) {}
	protected void onSetChannelKey(SetChannelKeyEvent<PircBotX> e) {}
	protected void onSetChannelLimit(SetChannelLimitEvent<PircBotX> e) {}
	protected void onSetInviteOnly(SetInviteOnlyEvent<PircBotX> e) {}
	protected void onSetModerated(SetModeratedEvent<PircBotX> e) {}
	protected void onSetNoExternalMessages(SetNoExternalMessagesEvent<PircBotX> e) {}
	protected void onSetPrivate(SetPrivateEvent<PircBotX> e) {}
	protected void onSetSecret(SetSecretEvent<PircBotX> e) {}
	protected void onSetTopicProtection(SetTopicProtectionEvent<PircBotX> e) {}
	protected void onSocketConnect(SocketConnectEvent<PircBotX> e) {}
	protected void onSuperOp(SuperOpEvent<PircBotX> e) {}
	protected void onTime(TimeEvent<PircBotX> e) {}
	protected void onTopic(TopicEvent<PircBotX> e) {}
	protected void onUnknown(UnknownEvent<PircBotX> e) {}
	protected void onUserList(UserListEvent<PircBotX> e) {}
	protected void onUserMode(UserModeEvent<PircBotX> e) {}
	protected void onVersion(VersionEvent<PircBotX> e) {}
	protected void onVoice(VoiceEvent<PircBotX> e) {}
	protected void onWhois(WhoisEvent<PircBotX> e) {}
	
	protected class MyListener extends ListenerAdapter<PircBotX> {
		public final ListenerPlugin plugin;
		
		public MyListener(ListenerPlugin plugin) {
			this.plugin = plugin;
		}
		
		public void onEvent(Event<PircBotX> e) {
			try {
				plugin.onEvent(e);
				super.onEvent(e);
			} catch (Exception ex) {}
		}
		public void onAction(ActionEvent<PircBotX> e) { plugin.onAction(e); }
		public void onChannelInfo(ChannelInfoEvent<PircBotX> e) { plugin.onChannelInfo(e); }
		public void onConnect(ConnectEvent<PircBotX> e) { plugin.onConnect(e); }
		public void onDisconnect(DisconnectEvent<PircBotX> e) { plugin.onDisconnect(e); }
		public void onFinger(FingerEvent<PircBotX> e) { plugin.onFinger(e); }
		public void onGenericChannel(GenericChannelEvent<PircBotX> e) { plugin.onGenericChannel(e); }
		public void onGenericChannelMode(GenericChannelModeEvent<PircBotX> e) { plugin.onGenericChannelMode(e); }
		public void onGenericChannelUser(GenericChannelUserEvent<PircBotX> e) { plugin.onGenericChannelUser(e); }
		public void onGenericCTCP(GenericCTCPEvent<PircBotX> e) { plugin.onGenericCTCP(e); }
		public void onGenericDCC(GenericDCCEvent<PircBotX> e) { plugin.onGenericDCC(e); }
		public void onGenericMessage(GenericMessageEvent<PircBotX> e) { plugin.onGenericMessage(e); }
		public void onGenericUser(GenericUserEvent<PircBotX> e) { plugin.onGenericUser(e); }
		public void onGenericUserMode(GenericUserModeEvent<PircBotX> e) { plugin.onGenericUserMode(e); }
		public void onHalfOp(HalfOpEvent<PircBotX> e) { plugin.onHalfOp(e); }
		public void onIncomingChatRequest(IncomingChatRequestEvent<PircBotX> e) { plugin.onIncomingChatRequest(e); }
		public void onIncomingFileTransfer(IncomingFileTransferEvent<PircBotX> e) { plugin.onIncomingFileTransfer(e); }
		public void onInvite(InviteEvent<PircBotX> e) { plugin.onInvite(e); }
		public void onJoin(JoinEvent<PircBotX> e) { plugin.onJoin(e); }
		public void onKick(KickEvent<PircBotX> e) { plugin.onKick(e); }
		public void onMessage(MessageEvent<PircBotX> e) { plugin.onMessage(e); }
		public void onMode(ModeEvent<PircBotX> e) { plugin.onMode(e); }
		public void onMotd(MotdEvent<PircBotX> e) { plugin.onMotd(e); }
		public void onNickAlreadyInUse(NickAlreadyInUseEvent<PircBotX> e) { plugin.onNickAlreadyInUse(e); }
		public void onNickChange(NickChangeEvent<PircBotX> e) { plugin.onNickChange(e); }
		public void onNotice(NoticeEvent<PircBotX> e) { plugin.onNotice(e); }
		public void onOp(OpEvent<PircBotX> e) { plugin.onOp(e); }
		public void onOwner(OwnerEvent<PircBotX> e) { plugin.onOwner(e); }
		public void onPart(PartEvent<PircBotX> e) { plugin.onPart(e); }
		public void onPing(PingEvent<PircBotX> e) { plugin.onPing(e); }
		public void onPrivateMessage(PrivateMessageEvent<PircBotX> e) { plugin.onPrivateMessage(e); }
		public void onQuit(QuitEvent<PircBotX> e) { plugin.onQuit(e); }
		public void onRemoveChannelBan(RemoveChannelBanEvent<PircBotX> e) { plugin.onRemoveChannelBan(e); }
		public void onRemoveChannelKey(RemoveChannelKeyEvent<PircBotX> e) { plugin.onRemoveChannelKey(e); }
		public void onRemoveChannelLimit(RemoveChannelLimitEvent<PircBotX> e) { plugin.onRemoveChannelLimit(e); }
		public void onRemoveInviteOnly(RemoveInviteOnlyEvent<PircBotX> e) { plugin.onRemoveInviteOnly(e); }
		public void onRemoveModerated(RemoveModeratedEvent<PircBotX> e) { plugin.onRemoveModerated(e); }
		public void onRemoveNoExternalMessages(RemoveNoExternalMessagesEvent<PircBotX> e) { plugin.onRemoveNoExternalMessages(e); }
		public void onRemovePrivate(RemovePrivateEvent<PircBotX> e) { plugin.onRemovePrivate(e); }
		public void onRemoveSecret(RemoveSecretEvent<PircBotX> e) { plugin.onRemoveSecret(e); }
		public void onRemoveTopicProtection(RemoveTopicProtectionEvent<PircBotX> e) { plugin.onRemoveTopicProtection(e); }
		public void onServerPing(ServerPingEvent<PircBotX> e) { plugin.onServerPing(e); }
		public void onServerResponse(ServerResponseEvent<PircBotX> e) { plugin.onServerResponse(e); }
		public void onSetChannelBan(SetChannelBanEvent<PircBotX> e) { plugin.onSetChannelBan(e); }
		public void onSetChannelKey(SetChannelKeyEvent<PircBotX> e) { plugin.onSetChannelKey(e); }
		public void onSetChannelLimit(SetChannelLimitEvent<PircBotX> e) { plugin.onSetChannelLimit(e); }
		public void onSetInviteOnly(SetInviteOnlyEvent<PircBotX> e) { plugin.onSetInviteOnly(e); }
		public void onSetModerated(SetModeratedEvent<PircBotX> e) { plugin.onSetModerated(e); }
		public void onSetNoExternalMessages(SetNoExternalMessagesEvent<PircBotX> e) { plugin.onSetNoExternalMessages(e); }
		public void onSetPrivate(SetPrivateEvent<PircBotX> e) { plugin.onSetPrivate(e); }
		public void onSetSecret(SetSecretEvent<PircBotX> e) { plugin.onSetSecret(e); }
		public void onSetTopicProtection(SetTopicProtectionEvent<PircBotX> e) { plugin.onSetTopicProtection(e); }
		public void onSocketConnect(SocketConnectEvent<PircBotX> e) { plugin.onSocketConnect(e); }
		public void onSuperOp(SuperOpEvent<PircBotX> e) { plugin.onSuperOp(e); }
		public void onTime(TimeEvent<PircBotX> e) { plugin.onTime(e); }
		public void onTopic(TopicEvent<PircBotX> e) { plugin.onTopic(e); }
		public void onUnknown(UnknownEvent<PircBotX> e) { plugin.onUnknown(e); }
		public void onUserList(UserListEvent<PircBotX> e) { plugin.onUserList(e); }
		public void onUserMode(UserModeEvent<PircBotX> e) { plugin.onUserMode(e); }
		public void onVersion(VersionEvent<PircBotX> e) { plugin.onVersion(e); }
		public void onVoice(VoiceEvent<PircBotX> e) { plugin.onVoice(e); }
		public void onWhois(WhoisEvent<PircBotX> e) { plugin.onWhois(e); }
	}
}