package client;

import java.awt.Color;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import myGameEngine.Avatars.TestAvatarController;
import myGameEngine.MyCamera.Camera3Pcontroller;
import myGameEngine.WorldObjects.MyCircle;
import myGameEngine.myActions.QuitGameAction;
import sage.app.BaseGame;
import sage.camera.ICamera;
import sage.camera.JOGLCamera;
import sage.display.IDisplaySystem;
import sage.event.EventManager;
import sage.event.IEventManager;
import sage.input.IInputManager;
import sage.input.InputManager;
import sage.input.action.IAction;
import sage.networking.IGameConnection.ProtocolType;
import sage.renderer.IRenderer;
import sage.scene.SceneNode;
import sage.scene.shape.Line;
import sage.scene.shape.Rectangle;
/*
 * This class handles the Local GameWorld 
 */
public class MyNetworkingClient extends BaseGame { 
	private String serverAddress;
	private int serverPort;
	private ProtocolType serverProtocol;
	private MyClient thisClient;
	
	private ICamera camera;
	private boolean isConnected;
	private TestAvatarController avatarController;
	private Camera3Pcontroller camController = null;
	private IEventManager eventMgr;
	private IInputManager im;
	private IRenderer renderer;
	
	public MyNetworkingClient(String serverAddr, int sPort) {
		super();
		this.serverAddress = serverAddr;
		this.serverPort = sPort;
		this.serverProtocol = ProtocolType.TCP;
		isConnected = false;
	}
	
	protected void initSystem(){
		//call a local method to create a DisplaySystem object
		// unable to get to work
		//IDisplaySystem display = createDisplaySystem();
		//setDisplaySystem(display);
		
		//create an Input Manager
		IInputManager inputManager = new InputManager();
		setInputManager(inputManager);
		
		//create an (empty) gameworld
		ArrayList<SceneNode> gameWorld = new ArrayList<SceneNode>();
		setGameWorld(gameWorld);
		
		super.initSystem();
	}
	
	protected void initGame() { // items as before, plus initializing network:
		try {
			
			thisClient = new MyClient(InetAddress.getByName(serverAddress), serverPort, serverProtocol, this);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		IDisplaySystem display = getDisplaySystem();
		eventMgr = EventManager.getInstance();
		im = getInputManager();
		renderer = display.getRenderer();
		display.setTitle("My Networking Client");

		createScene();
		createPlayer();
		initInput();
		
		if (thisClient != null) {
			thisClient.sendJoinMessage();
		}
		super.update((float) 0.0);
	}

	public void addToGameWorld(SceneNode n){
		addGameWorldObject(n);
	}
	
	public void removeFromGameWorld(SceneNode n){
		removeGameWorldObject(n);
	}
	
	private void createScene() {

		Line xAxis = new Line(new Point3D(-100, 0, 0), new Point3D(100, 0, 0), Color.red, 2);
		Line yAxis = new Line(new Point3D(0, -100, 0), new Point3D(0, 100, 0), Color.green, 2);
		Line zAxis = new Line(new Point3D(0, 0, -100), new Point3D(0, 0, 100), Color.blue, 2);
		
//		SceneNode ground = new Rectangle((float) 100, (float) 100);
//		Matrix3D groundM = ground.getLocalTranslation();
//		groundM.rotateX((double) 90);
//		groundM.translate((float) 50, (float) 50, (float) 0);
//		ground.setLocalTranslation(groundM);
//
//		addGameWorldObject(ground);
//		
		addGameWorldObject(xAxis);
		addGameWorldObject(yAxis);
		addGameWorldObject(zAxis);
	}

	private void createPlayer() {
		avatarController = new TestAvatarController(new MyCircle());
		addGameWorldObject(avatarController.getAvatar());
		
		camera = new JOGLCamera(renderer);
		camera.setPerspectiveFrustum(60, 2, 1, 1000);
		camera.setViewport(0.0, 1.0, 0.0, 1.0);
	}

	private void initInput() {
		String kbName = im.getKeyboardName();
		String gpName = im.getFirstGamepadName();
		camController = new Camera3Pcontroller(camera, avatarController.getAvatar(), im, gpName);
		

		// Quit Game Action
		IAction quitGame = new QuitGameAction(this);
		im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.ESCAPE, quitGame,
				IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
	}
	
	protected void update(float time) { // same as before, plus process any
										// packets received from server
		if (camController != null) {
			camController.update(time);
		}
		
		if (thisClient != null)
			thisClient.processPackets();
		

		super.update(time);
	}

	protected void shutdown() {
		if (thisClient != null) {
			try {
				thisClient.sendByeMessage();
				setIsConnected(false);
				thisClient.shutdown();
			} // shutdown() is inherited
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		super.shutdown();
	}

	public void setIsConnected(boolean b) {
		isConnected = b;	
	}
	
	public boolean IsConnected(){
		return isConnected;
	}

	public Vector3D getPlayerPosition() {
		return avatarController.getPosition();
	}
	
	protected void render() {
		renderer.setCamera(camera);
		super.render();
	}
}
