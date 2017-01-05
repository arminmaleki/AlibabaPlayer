package alibaba;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Paint;
import java.util.Map;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ChainedTransformer;

import alibaba.Player.setcolor;
import alibaba.Player.setcolor2;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.RadialTreeLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.renderers.VertexLabelAsShapeRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

public class GraphVis2 {
	Forest<String, String> g;
	VisualizationViewer<String, String> vv;
	TreeLayout<String, String> layout;
	
	// We create our graph in here
	// The Layout<V, E> is parameterized by the vertex and edge types
	public void draw(){
		
	
	//	layout = new TreeLayout<String, String>( g) ;
		
	//	g.addVertex( ""+1);
	//	g.addVertex("default6");
	//	g.addVertex(""+3);
		//layout.initialize();
		//layout = new TreeLayout<String, String>( g) ;
		//layout.setRepulsionRange(300);
		//((FRLayout)layout).setMaxIterations(1000);
			//layout.setSize(new Dimension(280, 180));
			
			// sets the initial size of the space
		
			// The BasicVisualizationServer<V,E> is parameterized by the edge types
		
		       
		    //   layout.setForceMultiplier(layout.getForceMultiplier()*1.0);
		    //   layout.setStretch(layout.getStretch()*1.25);
		       // vv.getRenderContext().setVertexFillPaintTransformer(new setcol);
		//vv.lay
		layout.setGraph(g);
		layout.initialize();
		//vv.setLayout((LayoutManager) layout);
		vv.setGraphLayout(new TreeLayout<String, String>( g,60,30) );
		    	vv.repaint();	
		    	
			  alibaba.AlibabaPlayer.paneltree.add(vv);
			  alibaba.AlibabaPlayer.paneltree.repaint();
		////	 g.addVertex( ""+1);
			//		g.addVertex("default6");
		//			g.addVertex(""+3);
			//		g.addEdge("1_3", "1","3");
			//	 g.addVertex("this");
			//layout.setGraph(g);
		//	layout.initialize();
			// JLabel l=new JLabel("HELLO");
			  
			    
		
	}
	//static class setlength implements SpringLayout.LengthFunction{}
	
	public GraphVis2(Transformer<String,Paint> sc2,Transformer<String,Paint> sc,Map<String, String> songHistory) {
		// Graph<V, E> where V is the type of the vertices
		// and E is the type of the edges
		g = new DelegateForest<String, String>();

	//	 g.addVertex( ""+1);
	//		g.addVertex("default6");
	//		g.addVertex(""+3);
	//		g.addEdge("1_3", "1","3");
		//g.addVertex( ""+1);
		//g.addVertex("default6");
		//g.addVertex(""+3);
		//g.addEdge("1_3", "1","3");
		layout = new TreeLayout<String, String>( g) ;
		//layout.setSize(new Dimension(280, 280));
		 vv = new VisualizationViewer<String, String>(layout);
			vv.setBackground(Color.WHITE);
			//vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<>());
			vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<>());
		//	vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<>());
			vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		//	vv.getRenderer().getVertexLabelRender
			vv.setPreferredSize(new Dimension(380, 180)); // Sets the viewing area
			//	vv.setBounds(100, 100, 100, 100);	
				// g.addVertex("this");// size
			final DefaultModalGraphMouse<String,Number> graphMouse = 
		            new DefaultModalGraphMouse<String,Number>();
			  vv.setGraphMouse(graphMouse);
		        vv.addKeyListener(graphMouse.getModeKeyListener());
			  graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
			
			  vv.getRenderContext().setVertexFillPaintTransformer(sc2);
			  VertexLabelAsShapeRenderer<String,String> vlasr = new VertexLabelAsShapeRenderer<String,String>(vv.getRenderContext());
			  vv.getRenderContext().setVertexLabelTransformer(
		        		// this chains together Transformers so that the html tags
		        		// are prepended to the toString method output
		        		new ChainedTransformer<String,String>(new Transformer[]{
		        		new ToStringLabeller<String>(),
		        		new Transformer<String,String>() {
							public String transform(String input) {
								
								if (songHistory.containsKey(input))return// "<html><center>Vertex<p>"+
										" "+songHistory.get(input).split("#")[0]+" ";
										else return input;
								
							}}})); 
			  vv.getRenderContext().setVertexShapeTransformer(vlasr);
		        vv.getRenderContext().setVertexLabelRenderer(new DefaultVertexLabelRenderer(Color.black));
		        vv.getRenderer().setVertexLabelRenderer(vlasr);
		     //   vv.getRenderContext().setVertexLabelRenderer(new DefaultVertexLabelRenderer(Color.black));
		        final ScalingControl scaler = new CrossoverScalingControl();
		        scaler.scale(vv, 0.6f, vv.getCenter());
		      
		//draw();
		// Add some vertices. From above we defined these to be type Integer.
	
		   
	}
}
