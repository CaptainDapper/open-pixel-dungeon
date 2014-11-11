package com.opd.openpixeldungeon;

import java.util.ArrayList;
import java.util.Arrays;

public class SubGames {
	private static ArrayList<? extends SubGame> buildSubGames = new ArrayList<SubGame>(Arrays.asList(
				new SubGame.classy(),
				new SubGame.vanilla(),
				new SubGame.shattered(),
				new SubGame.easy(),
				new SubGame.mofood()
			));
	private static ArrayList<SubGame> all = new ArrayList<SubGame>();
	
	private static boolean loaded = false; 
	
	@SuppressWarnings("unchecked")
	public static ArrayList<SubGame> load() {
		if (loaded) return all;
		
		OPDGame.Log("Loading SubGames from Build: " + buildSubGames.size());
		for(SubGame uGame : buildSubGames) {
			Boolean isFound = true;
			try {
				uGame.titleClass = (Class<? extends OPDScene>) Class.forName(uGame.titleSceneClassPath, false, SubGames.class.getClassLoader());
				//Class Exists
			} catch(ClassNotFoundException e) {
			    //No Class Found... WHAT DO!?
				isFound = false;
				OPDGame.Log("  " + uGame.refName + " SubGame titleClass Not Found!", true);
			}
			if (isFound) {
				OPDGame.Log("  " + uGame.refName + " loaded");
				all.add(uGame);
			}
		}
		
		loaded = true;
		return all;
	}
	
	/*private static void BLERGattachAnchors() {
		//THIS IS NOT USED PLZ
		String[] anchorPoints = { "" };
		for (String anchor : anchorPoints) {
			try {
				BufferedReader reader = new BufferedReader( new InputStreamReader( new FileInputStream(anchor + "Anchor.json") ) );
				if (reader != null) {
					JSONObject json = (JSONObject)new JSONTokener( reader.readLine() ).nextValue();
					reader.close();
					
					SubGame subGame = new SubGame();
					subGame.title = json.getString("title");
					subGame.refName = json.getString("refName");
					subGame.author = json.getString("author");
					subGame.version = json.getString("version");
					subGame.versionCode = json.getInt("versionCode");
					subGame.titleSceneClassPath = json.getString("titleSceneClassPath");
					subGame.asset = json.getString("asset");
					
					ClassLoader pcl = SubGames.class.getClassLoader();
					GameClassLoader gcl = new GameClassLoader(pcl);
					subGame.titleClass = gcl.loadClass(subGame.titleSceneClassPath);
					
					all.add(subGame);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				
			}
		}
	}
	
	private static class GameClassLoader extends ClassLoader {
		public GameClassLoader(ClassLoader parent) {
			super(parent);
		}

	    public Class<? extends OPDScene> loadClass(String name) throws ClassNotFoundException {
	        if(!"reflection.MyObject".equals(name))
	                return (Class<? extends OPDScene>)super.loadClass(name);

	        try {
	            String url = "/" + name.replace(".", "/") + ".java";
	            URL myUrl = new URL(url);
	            URLConnection connection = myUrl.openConnection();
	            InputStream input = connection.getInputStream();
	            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	            int data = input.read();

	            while(data != -1){
	                buffer.write(data);
	                data = input.read();
	            }

	            input.close();

	            byte[] classData = buffer.toByteArray();

	            return (Class<? extends OPDScene>)defineClass(name,
	                    classData, 0, classData.length);

	        } catch (MalformedURLException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        return null;
	    }
	}*/
}
