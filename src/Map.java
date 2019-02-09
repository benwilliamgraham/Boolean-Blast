import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_POLYGON_OFFSET_FILL;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPolygonOffset;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;

import org.joml.Vector3f;

public class Map {
	Model faces;
	Model edges;
	float[] shades;
	
	int X = 120, Y = 33, Z = 120;
	
	Block[][][] blocks;
	
	float gravity = 0.03f;
	
	Vector3f[] spawnPoints = {
		new Vector3f(59, 3, 53),
		new Vector3f(94, 13, 97),
		new Vector3f(30, 13, 105)
	};
	
	//colors
	float lightShade = 0.85f;
	float mediumShade = 0.3f;
	float darkShade = 0.05f;
	
	//items
	Camera activeCamera;
	Player player;
	ArrayList<Player> enemies = new ArrayList<Player>();
	ArrayList<Particle> particles = new ArrayList<Particle>();
	static Random random = new Random();
	
	Map(String filename){
		glClearColor(mediumShade, mediumShade, mediumShade, 1.0f);
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File(filename));
		} catch (IOException e) {
			System.err.println("Could not load map");
			e.printStackTrace();
			System.exit(-1);
		}
		
		blocks = new Block[X][Y][Z];
		
		//get collision data from map
		for(int z = 0; z < Z; z++) {
			for(int y = 0; y < Y - 1; y++) {
				for(int x = 0; x < X; x++) {
					blocks[x][Y - y - 2][z] = new Block(img.getRGB(x, y * Z + z) == -1, this);
				}
			}
		}
		
		//add a roof
		for(int z = 0; z < Z; z++) {
			for(int x = 0; x < X; x++) {
				blocks[x][Y - 1][z] = new Block(true, this);
			}
		}
		
		//add faces
		///////////////////////////////////////////////////////////////////
		
		ArrayList<Float> modelList = new ArrayList<Float>();
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		
		int ni = 0;
		for(int z = 0; z < Z; z++) {
			for(int y = 0; y < Y - 1; y++) {
				for(int x = 0; x < X; x++) {
					if(blocks[x][y][z].collides) {
						//x-
						if(x - 1 >= 0 && !blocks[x - 1][y][z].collides) {
							int bo = modelList.size() * 4;
							blocks[x][y][z].faceIndices.addAll(Arrays.asList(bo + 12, bo + 28, bo + 44, bo + 60));
							modelList.addAll(Arrays.asList(
									x - 0.5f, y - 0.5f, z + 0.5f, blocks[x][y][z].shade,
									x - 0.5f, y + 0.5f, z + 0.5f, blocks[x][y][z].shade,
									x - 0.5f, y - 0.5f, z - 0.5f, blocks[x][y][z].shade,
									x - 0.5f, y + 0.5f, z - 0.5f, blocks[x][y][z].shade 
							));
							
							indexList.addAll(Arrays.asList(
									ni + 0, ni + 1, ni + 3,
									ni + 0, ni + 3, ni + 2
							));
							ni += 4;
						}
						//x+
						if(x + 1 < X && !blocks[x + 1][y][z].collides) {
							int bo = modelList.size() * 4;
							blocks[x][y][z].faceIndices.addAll(Arrays.asList(bo + 12, bo + 28, bo + 44, bo + 60));
							modelList.addAll(Arrays.asList(
									x + 0.5f, y - 0.5f, z - 0.5f, blocks[x][y][z].shade,
									x + 0.5f, y + 0.5f, z - 0.5f, blocks[x][y][z].shade,
									x + 0.5f, y - 0.5f, z + 0.5f, blocks[x][y][z].shade,
									x + 0.5f, y + 0.5f, z + 0.5f, blocks[x][y][z].shade
							));
							
							indexList.addAll(Arrays.asList(
									ni + 0, ni + 1, ni + 3,
									ni + 0, ni + 3, ni + 2
							));
							ni += 4;
						}
						//y-
						if(y - 1 >= 0 && !blocks[x][y - 1][z].collides) {
							int bo = modelList.size() * 4;
							blocks[x][y][z].faceIndices.addAll(Arrays.asList(bo + 12, bo + 28, bo + 44, bo + 60));
							modelList.addAll(Arrays.asList(
									x - 0.5f, y - 0.5f, z - 0.5f, blocks[x][y][z].shade,
									x + 0.5f, y - 0.5f, z - 0.5f, blocks[x][y][z].shade,
									x - 0.5f, y - 0.5f, z + 0.5f, blocks[x][y][z].shade,
									x + 0.5f, y - 0.5f, z + 0.5f, blocks[x][y][z].shade
							));
							
							indexList.addAll(Arrays.asList(
									ni + 0, ni + 1, ni + 3,
									ni + 0, ni + 3, ni + 2
							));
							ni += 4;
						}
						//y+
						if(y + 1 < Y && !blocks[x][y + 1][z].collides) {
							int bo = modelList.size() * 4;
							blocks[x][y][z].faceIndices.addAll(Arrays.asList(bo + 12, bo + 28, bo + 44, bo + 60));
							modelList.addAll(Arrays.asList(
									x - 0.5f, y + 0.5f, z + 0.5f, blocks[x][y][z].shade,
									x + 0.5f, y + 0.5f, z + 0.5f, blocks[x][y][z].shade,
									x - 0.5f, y + 0.5f, z - 0.5f, blocks[x][y][z].shade,
									x + 0.5f, y + 0.5f, z - 0.5f, blocks[x][y][z].shade
							));
							
							indexList.addAll(Arrays.asList(
									ni + 0, ni + 1, ni + 3,
									ni + 0, ni + 3, ni + 2
							));
							ni += 4;
						}
						//z-
						if(z - 1 >= 0 && !blocks[x][y][z - 1].collides) {
							int bo = modelList.size() * 4;
							blocks[x][y][z].faceIndices.addAll(Arrays.asList(bo + 12, bo + 28, bo + 44, bo + 60));
							modelList.addAll(Arrays.asList(
									x - 0.5f, y - 0.5f, z - 0.5f, blocks[x][y][z].shade,
									x - 0.5f, y + 0.5f, z - 0.5f, blocks[x][y][z].shade,
									x + 0.5f, y - 0.5f, z - 0.5f, blocks[x][y][z].shade,
									x + 0.5f, y + 0.5f, z - 0.5f, blocks[x][y][z].shade 
							));
							
							indexList.addAll(Arrays.asList(
									ni + 0, ni + 1, ni + 3,
									ni + 0, ni + 3, ni + 2
							));
							ni += 4;
						}
						//z+
						if(z + 1 < Z && !blocks[x][y][z + 1].collides) {
							int bo = modelList.size() * 4;
							blocks[x][y][z].faceIndices.addAll(Arrays.asList(bo + 12, bo + 28, bo + 44, bo + 60));
							modelList.addAll(Arrays.asList(
									x + 0.5f, y - 0.5f, z + 0.5f, blocks[x][y][z].shade,
									x + 0.5f, y + 0.5f, z + 0.5f, blocks[x][y][z].shade,
									x - 0.5f, y - 0.5f, z + 0.5f, blocks[x][y][z].shade,
									x - 0.5f, y + 0.5f, z + 0.5f, blocks[x][y][z].shade
							));
							
							indexList.addAll(Arrays.asList(
									ni + 0, ni + 1, ni + 3,
									ni + 0, ni + 3, ni + 2
							));
							ni += 4;
						}		
					}
				}
			}
		}
		
		//convert arrays to primitives
		float[] model = new float[modelList.size()];
		for(int i = 0; i < modelList.size(); i++) { model[i] = modelList.get(i); }
		
		int[] indices = new int[indexList.size()];
		for(int i = 0; i < indexList.size(); i++) { indices[i] = indexList.get(i); }
		
		//load to model
		faces = new Model(model, indices);
		
		//add edges
		///////////////////////////////////////////////////////////////////
		/*0b _ _ _ _
		 *    \ \ \ \:(i + 1, j)
		 *     \ \ \:(i + 1, j + 1)
		 *      \ \:(i, j + 1)
		 *       \:(i, j)
		 */
		
		modelList = new ArrayList<Float>();
		indexList = new ArrayList<Integer>();
		
		ni = 0;
		int sv;
		for(int z = 0; z < Z - 1; z++) {
			for(int y = 0; y < Y - 1; y++) {
				for(int x = 0; x < X - 1; x++) {
					//xy+
					sv = 0;
					if(blocks[x][y][z].collides) sv += 0b1000; if(blocks[x][y + 1][z].collides) sv += 0b100; if(blocks[x + 1][y + 1][z].collides) sv += 0b10; if(blocks[x + 1][y][z].collides) sv += 0b1;
					if(sv != 0b0 && sv != 0b11 && sv != 0b110 && sv != 0b1001 && sv != 0b1100 && sv != 0b1111) {
						modelList.addAll(Arrays.asList(
								x + 0.5f, y + 0.5f, z - 0.5f, mediumShade,
								x + 0.5f, y + 0.5f, z + 0.5f, mediumShade
						));
						
						indexList.addAll(Arrays.asList(
								ni + 0, ni + 1
						));
						ni += 2;
					}
					//zy+
					sv = 0;
					if(blocks[x][y][z].collides) sv += 0b1000; if(blocks[x][y + 1][z].collides) sv += 0b100; if(blocks[x][y + 1][z + 1].collides) sv += 0b10; if(blocks[x][y][z + 1].collides) sv += 0b1;
					if(sv != 0b0 && sv != 0b11 && sv != 0b110 && sv != 0b1001 && sv != 0b1100 && sv != 0b1111) {
						modelList.addAll(Arrays.asList(
								x - 0.5f, y + 0.5f, z + 0.5f, mediumShade,
								x + 0.5f, y + 0.5f, z + 0.5f, mediumShade
						));
						
						indexList.addAll(Arrays.asList(
								ni + 0, ni + 1
						));
						ni += 2;
					}
					//xz+
					sv = 0;
					if(blocks[x][y][z].collides) sv += 0b1000; if(blocks[x + 1][y][z].collides) sv += 0b100; if(blocks[x + 1][y][z + 1].collides) sv += 0b10; if(blocks[x][y][z + 1].collides) sv += 0b1;
					if(sv != 0b0 && sv != 0b11 && sv != 0b110 && sv != 0b1001 && sv != 0b1100 && sv != 0b1111) {
						modelList.addAll(Arrays.asList(
								x + 0.5f, y - 0.5f, z + 0.5f, mediumShade,
								x + 0.5f, y + 0.5f, z + 0.5f, mediumShade
						));
						
						indexList.addAll(Arrays.asList(
								ni + 0, ni + 1
						));
						ni += 2;
					}
				}
			}
		}
		
		//convert arrays to primitives
		model = new float[modelList.size()];
		for(int i = 0; i < modelList.size(); i++) { model[i] = modelList.get(i); }
		
		indices = new int[indexList.size()];
		for(int i = 0; i < indexList.size(); i++) { indices[i] = indexList.get(i); }
		
		//load to model
		edges = new Model(model, indices);
	}
	
	void initializeItems(Window window) {
		player = new Player(window, spawnPoints[random.nextInt(spawnPoints.length)]);
		activeCamera = player.camera;
	}
	
	boolean checkCollision(Vector3f position) {
		return  (int)(position.x + 0.5f) < 0 ||
				(int)(position.x + 0.5f) >= X ||
				(int)(position.y + 0.5f) < 0 ||
				(int)(position.y + 0.5f) >= Y ||
				(int)(position.z + 0.5f) < 0 ||
				(int)(position.z + 0.5f) >= Z ||
				blocks[(int)(position.x + 0.5f)][(int)(position.y + 0.5f)][(int)(position.z + 0.5f)].collides;
	}
	
	void shadeBlock(Vector3f position, float shade) {
		blocks[(int)(position.x + 0.5f)][(int)(position.y + 0.5f)][(int)(position.z + 0.5f)].updateShade(shade, this);
	}
	
	void addParticles(Vector3f position, float count, float intensity) {
		for(int i = 0; i < count; i++) {
			particles.add(new Particle(new Vector3f(position), intensity, random));
		}
	}
	
	void update(Window window) {
		//update everything
		player.update(window, this);
		for(Player enemy: enemies) {
			//enemy.update(this);
		}
		for(int i = particles.size() - 1; i >= 0; i--) {
			if(particles.get(i).update(this)) {
				particles.remove(i);
			}
		}
	}
	
	void render(Camera camera, ShaderProgram shaderProgram) {
		shaderProgram.start();
		
		//setup
		shaderProgram.loadMatrix(shaderProgram.u_MVP, camera.VP);
		
		//draw edges
		glDisable(GL_POLYGON_OFFSET_FILL);
		glBindVertexArray(edges.vao);
		glDrawElements(GL_LINES, edges.index_count, GL_UNSIGNED_INT, 0);
		
		//draw polygons
		glEnable(GL_POLYGON_OFFSET_FILL);
		glPolygonOffset(1.0f, 0.001f);
		glBindVertexArray(faces.vao);
		glDrawElements(GL_TRIANGLES, faces.index_count, GL_UNSIGNED_INT, 0);
		
		//draw items
		for(Player enemy: enemies) {
			enemy.render(activeCamera, shaderProgram);
		}
		for(Particle particle: particles) {
			particle.render(activeCamera, shaderProgram);
		}
	}
}
