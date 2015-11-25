package Networking;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

public class Matchmaker {
	
	private static final String defaultChannel = "match_head";
	private static final String allChannel = "match_all";
	private Pubnub pubnub;
	private String uuid;
	private String name = "";
	private int token = 1;
	private int tolerance = 0;
	private JSONArray match = null;
	private boolean searching = false;
	private JSONObject stack = new JSONObject();
	private int partySize = 0;
	private int allSize = 0;
	
	/**
	 * Internally used to set up the Matchmaking instance, given a pubnub publish and subscribe key.
	 * @param publish
	 * @param subscribe
	 */
	private void newMatchmaker(String publish, String subscribe) {
		pubnub = new Pubnub(publish, subscribe);
		uuid = pubnub.getUUID();
		
		try {
			pubnub.subscribe(defaultChannel, new Callback() {
				
				@Override
				public void connectCallback(String channel, Object message) {
					System.out.println("Connect: " + channel + ": " + message);
				}
				
				@Override
				public void disconnectCallback(String channel, Object message) {
					System.out.println("Disconnect: " + channel + ": " + message);
				}
				
				@Override
				public void reconnectCallback(String channel, Object message) {
					
				}
				
				@Override
				public void successCallback(String channel, Object message) {
					System.out.println("Success: " + channel + ": " + message);
				}
				
				@Override
				public void errorCallback(String channel, Object message) {
					System.out.println("Error: " + channel + ": " + message);
				}
				
			});
			
			pubnub.subscribe(allChannel, new Callback() {});
			
			pubnub.presence(allChannel, new Callback() {
				
				@Override
			    public void connectCallback(String channel, Object message) {
					pubnub.hereNow(allChannel, true, true, new Callback() {
						@Override
						public void successCallback(String channel, Object message) {
							try {
								allSize = ((JSONObject)message).getJSONArray("uuids").length();
							} catch (JSONException e) {}
						}
					});
			    }
			 
			    @Override
			    public void disconnectCallback(String channel, Object message) {
			    	pubnub.hereNow(allChannel, true, true, new Callback() {
						@Override
						public void successCallback(String channel, Object message) {
							try {
								allSize = ((JSONObject)message).getJSONArray("uuids").length();
							} catch (JSONException e) {}
						}
					});
			    }
				
			});
			
			pubnub.subscribe(uuid, new Callback() {
				
				@Override
				public void successCallback(String channel, Object message) {
					JSONObject msg = (JSONObject)message;
					try {
						if (msg.has("internal")) {
							if (msg.getString("method") == "join" && searching) {
								match = msg.getJSONArray("match");
								searching = false;
								setToken(token);
								setupConnect();
							} else if (msg.getString("method") == "disconnect" && msg.getBoolean("perm")) {
								if (match != null) {
									for (int i = 0; i < match.length(); i++) {
										if (match.getJSONObject(i).getString("uuid") == msg.getString("value")) {
											match.remove(i);
											pubnub.unsubscribePresence(msg.getString("value"));
											if (stack.has("disconnect")) {
												Callback callback = (Callback)stack.get("disconnect");
												callback.successCallback(msg.getString("value"), message);
											}
										}
									}
								}
							}
						} else {
							if (msg.has("method") && stack.has(msg.getString("method"))) {
								Callback callback = (Callback)stack.get(msg.getString("method"));
								callback.successCallback(uuid, message);
							}
						}
					} catch (JSONException e) {}
				}
				
				@Override
				public void errorCallback(String channel, Object message) {
					System.out.println("Error: " + channel + ": " + message);
				}
				
			});
			
		} catch (PubnubException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Set up our class and create a connection to our main channel, provided a publish and subscribe key obtained from https://www.pubnub.com/
	 * @param publish key
	 * @param subscribe key
	 */
	public Matchmaker(String publish, String subscribe) {
		newMatchmaker(publish, subscribe);
	}
	
	/**
	 * Shortcut for creating a Matchmaker instance given a file containing the publish and subscribe key, like
	 * line 1 = publish key
	 * line 2 = subscribe key
	 * @param file containing publish and subscribe keys
	 * @throws Exception 
	 */
	public Matchmaker(File keyfile) throws Exception {
		String pub = null;
		String sub = null;
		if (keyfile.canRead()) {
			try {
				FileReader fr = new FileReader(keyfile);
				BufferedReader br = new BufferedReader(fr);
				pub = br.readLine();
				sub = br.readLine();
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new Exception(keyfile.exists() ? "The specified file is not readable." : "The specified file does not exist.");
		}
		if (pub != null && sub != null) {
			newMatchmaker(pub, sub);
		} else {
			throw new Exception("Publish and subscribe keys are not supplied in the key file.");
		}
	}
	
	/**
	 * Internally used to begin listen to other channels' presence for leave events.
	 */
	private void setupConnect() {
		if (match != null) {
			for (int i = 0; i < match.length(); i++) {
				try {
					pubnub.presence(match.getJSONObject(i).getString("uuid"), new Callback() {
						
						@Override
					    public void connectCallback(String channel, Object message) {
							try {
								if (stack.has("connect")) {
									Callback callback = (Callback)stack.get("connect");
									callback.successCallback(channel, new Message("connect", channel).put("internal", true).put("perm", false));
								}
							} catch (JSONException e) {}
							partySize++;
					    }
					 
					    @Override
					    public void disconnectCallback(String channel, Object message) {
					    	try {
								if (stack.has("disconnect")) {
									Callback callback = (Callback)stack.get("disconnect");
									callback.successCallback(channel, new Message("disconnect", channel).put("internal", true).put("perm", false));
								}
							} catch (JSONException e) {}
					    	partySize--;
					    }
						
					});
				} catch (Exception e) {}
			}
			partySize = match.length();
		} else {
			partySize = 0;
		}
	}
	
	/**
	 * Leave all channels, quitting our connection
	 */
	public void close() {
		pubnub.unsubscribeAll();
		pubnub.shutdown();
		searching = false;
		match = null;
	}
	
	/**
	 * Attach or overwrite a callback set for a specific method. E.g. setCallback("shoot", Callback) to have the callback's successCallback method
	 * fire whenever the player shoots
	 * @param method
	 * @param callback
	 */
	public void setCallback(String method, Callback callback) {
		try {
			stack.put(method, callback);
		} catch (JSONException e) {}
	}
	
	/**
	 * Disconnect a callback in the stack
	 * @param method
	 */
	public void clearCallback(String method) {
		stack.remove(method);
	}
	
	/**
	 * Sets the tolerance in which we detect if you are on an equal level to your opponent
	 * @param tolerance
	 */
	public void setTolerance(int tolerance) {
		this.tolerance = Math.abs(tolerance);
	}
	
	/**
	 * Get the maximum difference in skill between you and an opponent. Defaults to 0
	 * @return tolerance
	 */
	public int getTolerance() {
		return tolerance;
	}
	
	/**
	 * Gets the amount of players in your match, excluding yourself.
	 * @return size of match
	 */
	public int getSize() {
		return partySize;
	}
	
	/**
	 * Checks if you are currently in a given channel (match_head is our main channel)
	 * @param channel name
	 * @return boolean indicating whether you are subscribed to the specified channel
	 */
	public boolean inChannel(String channelName) {
		return Arrays.asList(pubnub.getCurrentlySubscribedChannelNames().split(",")).contains(channelName);
	}
	
	/**
	 * Finds the total amount of online players in our system
	 * @return amount of online players
	 */
	public int allUsers() {
		return allSize;
	}
	
	/**
	 * Set a number used for optimizing matchmaking by skill. Defaults to 1
	 * @param token
	 */
	public void setToken(int token) {
		JSONObject jso = new JSONObject();
		try {
			jso.put("token", token);
			jso.put("name", name);
			jso.put("search", searching);
		} catch (JSONException e) {}
		this.token = token;
		pubnub.setState(defaultChannel, uuid, jso, new Callback() {});
	}
	
	/**
	 * Gets the number specifying your skill to be used with to match with
	 * @return token
	 */
	public int getToken() {
		return token;
	}
	
	/**
	 * Set your current player's name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
		setToken(token);
	}
	
	/**
	 * Get your current player's name, defaults to a GUID
	 * @return player name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns an unique string used to identify your user
	 * @return unique GUID string
	 */
	public String getUUID() {
		return uuid;
	}
	
	/**
	 * Yield the current thread
	 * @param milli seconds
	 */
	public void sleep(int millisec) {
		try {
			Thread.sleep(millisec);
		} catch (InterruptedException e) {}
	}
	
	/**
	 * Checks if you are currently in a match
	 * @return boolean indicating if you are in a match
	 */
	public boolean inMatch() {
		return match != null;
	}
	
	/**
	 * Checks if you are currently searching for a match
	 * @return boolean indicating if you are searching for a match
	 */
	public boolean isSearching() {
		return searching;
	}
	
	/**
	 * Internally force a message to a remote channel
	 * @param channel
	 * @param message
	 */
	private void internalSend(String channel, JSONObject message) {
		pubnub.publish(channel, message, new Callback() {
			
			@Override
			public void errorCallback(String channel, PubnubError resp) {
				if (resp.errorCode == 0 && !resp.getErrorString().equals("Message Too Large")) {
					internalSend(channel, message);
				}
			}
			
		});
	}
	
	/**
	 * Broadcast a message to everyone in your current match
	 * @param message
	 */
	public void send(JSONObject message) {
		if (message.has("internal")) {
			message.remove("internal");
		}
		if (match != null) {
			for (int i = 0; i < match.length(); i++) {
				try {
					internalSend(match.getJSONObject(i).getString("uuid"), message);
				} catch (JSONException e) {}
			}
		}
	}
	
	/**
	 * Begin searching for one or more other players. Use leave() to stop searching
	 * @param match size
	 */
	public void search(int size) {
		// set state to 'searching' for player
		searching = true;
		setToken(token);
		
		pubnub.hereNow(defaultChannel, true, true, new Callback() {
			
			@Override
			public void successCallback(String channel, Object message) {
				if (searching) {
					try {
						JSONArray list = ((JSONObject)message).getJSONArray("uuids");
						JSONArray tmatch = new JSONArray();
						for (int j = 4; j >= 1; j--) {
							for (int i = 0; i < list.length(); i++) {
								JSONObject obj = list.getJSONObject(i);
								if (obj.has("state") && !obj.getString("uuid").equals(uuid) && obj.getJSONObject("state").getBoolean("search") &&
										tmatch.length() < size) {
									if (Math.abs(token - obj.getJSONObject("state").getInt("token")) <= tolerance / j) {
										tmatch.put(obj);
									}
								}
							}
						}
						match = null;
						if (tmatch.length() < size) { // not enough people, retry
							sleep(1000);
							search(size);
						} else {
							match = tmatch;
							searching = false;
							setToken(token);
							JSONObject ping = new JSONObject();
							ping.put("internal", true);
							ping.put("method", "join");
							ping.put("match", match);
							for (int i = 0; i < match.length(); i++) {
								try {
									internalSend(match.getJSONObject(i).getString("uuid"), ping);
								} catch (JSONException e) {}
							}
							setupConnect();
						}
					} catch (JSONException e) {}
				} else {
					match = null;
				}
			}
			
			@Override
			public void errorCallback(String channel, Object message) {
				System.out.println("Error: " + channel + "; " + message);
			}
			
		});
	}
	
	/**
	 * Stops searching for a match
	 */
	public void leave() {
		if (searching) {
			searching = false;
			if (match != null) {
				for (int i = 0; i < match.length(); i++) {
					try {
						internalSend(match.getJSONObject(i).getString("uuid"), new Message("disconnect", uuid).put("internal", true).put("perm", true));
						pubnub.unsubscribePresence(match.getJSONObject(i).getString("uuid"));
					} catch (JSONException e) {}
				}
			}
			match = null;
			setToken(token);
		}
	}
	
}
