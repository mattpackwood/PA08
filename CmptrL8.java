/* Matt Packwood, Orchard Ridge Campus, Monday Evening Class, Fall Semester 2003
 *
 * PA08: Use inheritance concepts to extend another computer class from the base 
 * model class created in PA07.  The new computer is the base model with no
 * keyboard, a track ball instead of a mouse, a different color and new CPU
 * features: a standard ZIP drive, speakers, microphone and an optional
 * flash card reader.
 *
 * The applet class is modified to create model computer objects from both the
 * base and extended model classes and the ZIP button is changed to a generic
 * option button to accomodate both the ZIP option for the base model and the
 * flash reader option for the new model. The base model class is unchanged.
 *  
 * The new model class extends the base class and implements the new features
 * by replacing, augmenting and retarding selected methods from the base class.
 *
 */
import java.awt.*;
import java.applet.*;
import java.awt.event.*; // event lib
 
public class CmptrL8 extends Applet //** APPLET CLASS **
		implements AdjustmentListener, ActionListener { // mixed events
	Button selctB, optB; // buttons
	Scrollbar xBar, ballBar; // scrollbar vars
	boolean optF= false, homeF= true; // logic switchs
	int hX, wX, hY, wY, hS, wS; // base vars; h= home, w= work
	int sW, sH; // applet screen width/Ht
	int wH= 63; // space for widgets at top of screen...
	int minX= 9, maxX, incrX= 9; // base x limits and increment
	int ballCt= 0;
		// define ** OBJECT REFS ** here....
	CompSys home, actv ;
	CompSysNew work ;
	
public void init () {
	setBackground (Color.white); // BG color
	Dimension size= getSize ( ); // get screen size
	sW= size.width; sH= size.height; // set screen constraints
	hS= sH/14; // initial home CPU ht
	wS= sH/12; // initial work CPU ht
	maxX= sW-(6*wS); // right edge minus offset 
	hX= minX; // home on left edge
	wX= maxX; // work on right edge
	hY= (int) (sH * 0.75f); // both 1/4 above screen bot
	wY= (int) (sH * 0.75f);
	add (new Label ("toggle buttons for home/work computer and CPU options; "+
		"Scrollbars: X= << move sideways >>; balls = -/+ screen balls "));  
	selctB= new Button ("WORK");
	add (selctB);
	selctB.addActionListener (this);
	optB= new Button ("OPTION");
	add (optB);
	optB.addActionListener (this);
	add (new Label ("X >>"));
	xBar= new Scrollbar (Scrollbar.HORIZONTAL, wX, 1, 0, maxX);
		 // constrain to stay within screen
	add (xBar);
	xBar.addAdjustmentListener (this);
	xBar.setUnitIncrement (incrX); // override default with x incr
 	add (new Label ("balls >>"));  
	ballBar= new Scrollbar (Scrollbar.HORIZONTAL, 0, 1, 0, 7); // constrain for 0-6 balls
	add (ballBar);
	ballBar.addAdjustmentListener (this);
		// ** CONSTRUCT MODEL OBJECTS **
	home= new CompSys (hX,hY,hS);
	work= new CompSysNew (wX,wY,wS);
		// ** INIT ACTV OBJECT REF **
	actv= work;	

   	}
public void paint (Graphics g) {
	ballCt= actv.getBallCt(); // ** GETTER METHOD CALLS **
	optF= actv.getOptStat();
	hX= home.getX();
	wX= work.getX();
	showStatus ("PEx 8:"+(homeF ? "HOME" : "WORK") + " actv; ballCt="+ ballCt+ ", OPT= "+optF+", homeX="+hX+", workX="+wX); 
		// ** OBJECT DISPLAY CALLS here **
		home.dsplyFig (g);
		work.dsplyFig (g);
		// display names beneath objects
		g.setColor (Color.black);
		g.drawString ("Home", hX+2*hS, hY+2*hS);
		g.drawString ("Work", wX+2*wS, wY+2*wS);
	}
public void actionPerformed (ActionEvent e) {
	if (e.getSource ( ) == selctB) { // ** SWITCH ACTV OBJECT **
		// toggle actv model switch
		homeF = ! homeF; // toggle
		if (homeF) {
			// update button label
			selctB.setLabel ("WORK");
			// update actv obj ref
			actv=work;
			}
		else { // homeF false implies work....
			// update button label
			selctB.setLabel ("HOME");
			// update actv obj ref
			actv=home;
			}
		xBar.setValue (actv.getX ( )); // update SB ctrs
		ballBar.setValue (actv.getBallCt ( ));
		}
	else if (e.getSource ( ) == optB)  // add/remove optional component
		// toggle actv optional component control
		actv.toglOptStat ( );
	repaint ( );
	}
public void adjustmentValueChanged (AdjustmentEvent e) { // SB events
	// set actv X value from scrollbar
	actv.setX (xBar.getValue ( )); // new val for actv fig 
	// set actv ball ct value from scrollbar 
	actv.setBallCt (ballBar.getValue ( )); 
	repaint ( ); // update display
	}
} // ** END APPLET CLASS **

class CompSys { // ** BASE MODEL CLASS **
// same as C9 PA# 7 except for changes noted below
	// ** INSTANCE VARS; COPIED TO EACH OBJECT DURING CONSTRUCTION
	int bX, bY, bS; // base vars
	int qS, hS, dS; // work ratio vars; yours may vary... 
	int ballCt; // ball ctr
	boolean zipF; // ZIP option on/off
	Graphics g; // class-scope screen ref
	Color sysClr = (Color.yellow); // change Color.yellow refs to sysClr
// ** PUBLIC, INTERACTION METHODS **
public CompSys (int x, int y, int s) { // ** CONSTRUCTOR **
	// init global base, ballCt and zipF vals here
	bX = x;
	bY = y;
	bS = s;
	}
public void setX (int x) { // ** SETTER METHODS **
	// update bX here
	bX=x;
	}
public void setBallCt (int ct) {
	// update ballCt here
	ballCt = ct;
	}
public void toglOptStat ( ) { // change toglZIP refs to toglOpt 
	zipF= ! zipF;
	}
public boolean getOptStat ( ) {
	return zipF;
	}
public int getBallCt ( ) {
	return ballCt;
	}
public int getX ( ) {
	return bX;
	}
public void dsplyFig (Graphics gg) {
	g= gg;
	calcRatios ( );
	dsplyKeyBord ( );
	dsplyPtrDev ( ); 
	dsplyCPU ( );
	dsplyDsply ( );
	}
void calcRatios ( ) { 
	// same old same old......
	qS= Math.round (bS/4.0f); // calc ratio vals
	hS= Math.round (bS/2.0f); // calc ratio vals
	dS= Math.round (bS*2.0f); // calc ratio vals
	}
void dsplyCPU ( ) { 
	g.setColor (sysClr); //draw stuff
	// CPU, CD, opt ZIP
	g.fillRect (bX, bY-bS, dS+dS+bS, bS); // CPU
	g.setColor (Color.black);
	g.drawRect (bX, bY-bS, dS+dS+bS, bS); // CPU outline
	g.fillRect (bX+dS+hS, bY-hS-qS, dS, qS); // DVD slot
	// conditional ZIP slot display; see hat dsply technique in SF demo
	if (zipF) {
		g.fillRect (bX+hS, bY-bS+qS, bS, qS);
		}
	}
void dsplyKeyBord ( ) {
	// KB & keys
	g.setColor (sysClr);
	g.fillRect (bX, bY, dS+dS, bS); // Keyboard
	g.setColor (Color.black);
	g.drawRect (bX, bY, dS+dS, bS); // Keyboard Outline
	g.fillRect (bX+qS, bY+qS, dS+bS, hS); // Keys
	}
void dsplyPtrDev ( ) {
	// mouse, buttons & cord
	g.setColor (sysClr);
	g.fillOval (bX+dS+dS+qS, bY+qS, hS, hS); // Mouse
	g.setColor (Color.black);
	g.drawOval (bX+dS+dS+qS, bY+qS, hS, hS); // Mouse outline
	g.drawLine (bX+dS+dS, bY+hS, bX+dS+dS+qS, bY+hS); // Mouse cord
	g.fillArc (bX+dS+dS+qS, bY+qS, hS, hS, 45, 90); // Mouse 1
	g.fillArc (bX+dS+dS+qS, bY+qS, hS, hS, 225, 90); // Mouse 2
	}
void dsplyDsply ( ) {
	// monitor, screen & opt balls
	g.setColor (sysClr);
	g.fillRect (bX+hS, bY-dS-dS, dS+dS, dS+bS); // Monitor
	g.setColor (Color.black);
	g.drawRect (bX+hS, bY-dS-dS, dS+dS, dS+bS); // Monitor Outline
	g.setColor (Color.lightGray);
	g.fillRect (bX+hS+qS, bY-dS-bS-hS-qS, dS+bS+hS, dS+hS); // Screen
	// conditional screen ball display; see hair display technique in SF demo
	switch (ballCt) { // no breaks == fall-thru
		case 6: 	g.setColor (Color.black);
				g.fillOval (bX+bS+dS-(((ballCt-3)%3)*bS), bY-dS-hS, bS, bS);
		case 5: 	g.setColor (Color.red);
				g.fillOval (bX+bS+dS-(((ballCt-2)%3)*bS), bY-dS-hS, bS, bS);   
		case 4: 	g.setColor (Color.yellow);
				g.fillOval (bX+bS+dS-(((ballCt-1)%3)*bS), bY-dS-hS, bS, bS);
		case 3: 	g.setColor (Color.green);
				g.fillOval (bX+bS+dS-(((ballCt-3)%3)*bS), bY-dS-hS-((ballCt <=5 ? 0 : 1)*bS), bS, bS);
		case 2: 	g.setColor (Color.blue);
				g.fillOval (bX+bS+dS-(((ballCt-2)%3)*bS), bY-dS-hS-((ballCt <=4 ? 0 : 1)*bS), bS, bS);
		case 1: 	g.setColor (Color.white);
				g.fillOval (bX+bS+dS-(((ballCt-1)%3)*bS), bY-dS-hS-((ballCt <=3 ? 0 : 1)*bS), bS, bS);
		} // end switch; minus values ignored
	}
} // ** END BASE MODEL CLASS **

class CompSysNew extends CompSys { // ** EXTENDED MODEL CLASS **
// base+ZIP+speakers+microphone+opt rdr+track ball vs mouse+cyan vs yellow - KB
boolean rdrF; // flash rdr cntrl
int eS; // 1/8 base size ratio
public CompSysNew (int x, int y, int s) { // constructor
	// call base constructor
	super (x, y, s); 
	sysClr= (Color.cyan); // new color
	// set cntrls to zip on, rdr off
	zipF = true ;
	rdrF = false ;
	}
public void toglOptStat ( ) { 
	// replace base ZIP opt w/flash rdr opt
	rdrF = ! rdrF ;
	}
public boolean getOptStat ( ) {
	// replace base ZIP opt w/flash rdr opt
	return rdrF;
	}
void dsplyKeyBord ( ) {
	// retard base; no KB for new
	}
void dsplyPtrDev ( ) { // replace base
	// trackball, buttons & housing
	g.setColor (sysClr);
	g.fillRect (bX+dS+dS, bY, bS, bS); // Trackball
	g.setColor (Color.white);
	g.fillArc (bX+dS+dS-qS, bY, hS, bS, -90, 180);
	g.fillArc (bX+dS+dS+hS+qS, bY, hS, bS, 90, 180);
	g.setColor (Color.lightGray);
	g.fillOval (bX+dS+dS+qS, bY+qS, hS, hS);
	g.setColor (Color.black);
	g.drawRect (bX+dS+dS, bY, bS, bS);
	g.drawArc (bX+dS+dS-qS, bY, hS, bS, -90, 180);
	g.drawArc (bX+dS+dS+hS+qS, bY, hS, bS, 90, 180);
	g.drawOval (bX+dS+dS+qS, bY+qS, hS, hS);
	}	
void dsplyCPU ( ) { // augment base w/new features
	eS= Math.round (bS* 0.125f); // calc addtl ratio
	// call base CPU
	super.dsplyCPU();
	newFeatures ( ); // added features 
	}
void newFeatures ( ) { // added method; not in base
	// speakers, microphone & opt flash rdr
	g.setColor (sysClr);
	g.fillRect (bX, bY-dS, hS, bS); // Left Hand Speaker
	g.fillRect (bX+dS+dS+hS, bY-dS, hS, bS); // Right Hand Speaker
	g.setColor (Color.darkGray);
	g.fillOval (bX+eS, bY-dS+eS, qS, hS+qS); // Left Hand Speaker 
	g.fillOval (bX+dS+dS+hS+eS, bY-dS+eS, qS, hS+qS); // Right Hand Speaker
	g.setColor (Color.black);
	g.fillOval (bX+dS+qS+eS+eS, bY-qS-eS, qS, qS); // Microphone
	g.drawRect (bX, bY-dS, hS, bS); // Left Hand Speaker
	g.drawRect (bX+dS+dS+hS, bY-dS, hS, bS); // Right Hand Speaker
	if (rdrF) {
		g.fillRect (bX+dS, bY-bS+qS, qS, hS); // Flash Reader
		}	
	}
} // ** END EXTENDED MODEL CLASS **
