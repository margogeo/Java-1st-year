"use strict";

function error(s) {
   //console.log('!' + s);  //Uncomment to see errors in the terminal
   throw(s);	
}

class Const {
  constructor(constVal) {
    this.value = constVal;
  }
  evaluate(x, y, z) {
	return this.value;
  }
  prefix() {
	return this.toString();
  }
  toString() {
	return "" + this.value;
  }
}

class Variable {
  constructor(x) {
	this.name = x;
  }
  evaluate(x,y,z) {
	switch(this.name) {
		case 'x': return x;
		case 'y': return y;
		case 'z': return z;
	}
  }
  prefix() {
	return this.name;
  }
  toString() {
	return this.name;
  }	
}

class Operation {
  constructor(a, b, c, fun) {
    this.a = a;
    this.b = b;
	this.op = c;
	this.f = fun;
  }
  prefix() {
	return "(" + this.op.substr(1) + " " + this.a.prefix() + " " +this.b.prefix() + ")";
  }
  toString() {
	return this.a.toString() + " " + this.b.toString() + this.op;
  }
  evaluate(x,y,z) {
	return this.f(this.a.evaluate(x,y,z), this.b.evaluate(x,y,z));
  }
}

class Unary {
  constructor(a, c, fun) {
    this.a = a;
	this.op = c;
	this.f = fun;
  }
  prefix() {
	return "(" + this.op.substr(1) + " " +this.a.prefix() + ")";
  }
  toString() {
	return this.a.toString() + this.op;
  }
  evaluate(x,y,z) {
	return this.f(this.a.evaluate(x,y,z));
  }
}

class Add extends Operation {
  constructor(a, b) {
    super(a, b, " +", function(a,b) { return a + b; } );
  }
} 

class Subtract extends Operation {
  constructor(a, b) {
    super(a, b, " -", function(a,b) { return a - b; } );
  }
} 

class Multiply extends Operation {
  constructor(a, b) {
    super(a, b, " *", function(a,b) { return a * b; } );
  }
} 

class Divide extends Operation {
  constructor(a, b) {
    super(a, b, " /", function(a,b) { return a / b; } );
  }
} 

class Negate extends Unary {
  constructor(x) {
	super(x," negate", function(x) { return -x; } );
  }
}

class Sinh extends Unary {
  constructor(x) {
	super(x," sinh", function(x) { return Math.sinh(x); } );
  }
} 

class Cosh extends Unary {
  constructor(x) {
	super(x," cosh", function(x) { return Math.cosh(x); } );
  }
} 

function isDigit(c) {
  return ("0123456789".indexOf(c) >= 0);
}

var parsedString = "";

//Extract single lexic items
function lexics(s) {
 parsedString = s;
 var m = [];
 let i = 0, l = s.length, k = 0, p = 0; 

 for(;;) {
	while (i < l && s.charAt(i) == ' ') {
		i++;
	}
	if(i >= l) {
		if(p > 0) {
			error("Unclosed parenthesis: " + s);
		}
		return m;
	}
	var el = { t:' ' , v: "", pos: i };
	let c = s.substr(i, 1), nc = null;
	if(i < l - 1) 	
		nc = s.substr(i + 1, 1);

	if (c == "(")  {
		el.t = 'L';   
		p++;
		i++;
    }
	else if(c == ")") {
		el.t = 'R';
		if(p <= 0) {
			error("Unpaired parenthesis:" + s);
		}
		p--;
		i++;
	}
	else if (isDigit(c) || c =="-" && nc != null && isDigit(nc)) {
		let n = i;
		while(n < s.length) {
			let nc = s.charAt(++n);
			if(nc < '0' || nc > '9')
				break;
		}
		el.v = s.substr(i, n - i);
		el.t = 'N';
		i = n;
	}
	else if  ("+-*/".indexOf(c) >= 0) {
		el.t = 'O';
		el.v = c;
		i++;
	}
	else if  ("xyz".indexOf(c) >= 0) {
		if(i < l - 1 && s.charAt(i + 1) >= 'A')
			error("Wrong variable name: " + s.substr(i));
		el.t = 'V';
		el.v = c;
		i++;
	}
	else {
		let j = i;
		while (j < l && s.charAt(j) >= 'A') {
			j++;
		}
		if (j > i) {
			let fc = s.substr(i, j-i);
			if (fc == "sinh" || fc == "cosh" || fc == "negate") {
				el.t = 'S';
				el.v = fc;
				i = j;
			}
		}
	}
	
	if (el.t == ' ') {
		error("Unrecognized symbol: " + s.substr(i));
	}
	m[k++] = el;
 }
}

function parseOper(ob, opIndex)
{
	if(ob.i >= ob.num)
		return null;

	let top = ob.m[ob.i];
	if (opIndex != 2 && top.t == 'L' && ob.i > 0 && ob.m[ob.i - 1].t != 'O' && ob.m[ob.i - 1].t != 'V' && ob.m[ob.i - 1].t != 'S') {
		error("Wrong parenthesis usage " + ob.m[ob.i - 1].t);
	}	

	while ((top.t == 'L' || top.t == 'R') && ob.i < ob.num - 1) {
		top = ob.m[++ob.i];
	}

	if (opIndex != 1 && ob.i < ob.num - 1 && (top.t == 'N' || top.t == 'V')) {
		let next = ob.m[ob.i+1];
		if (next.t == 'V' || next.t == 'N' || next.t == 'O') {
			error("Two operands without operation: " + parsedString.substr(ob.m[ob.i].pos));	
		}
	} 	

	if(top.t == 'N') {
		ob.i++;
		return new Const(parseInt(top.v));
	}

	if(top.t == 'V') {
		ob.i++;
		return new Variable(top.v);
	}

	if(top.t == 'S') {
		ob.i++;
		let prm = parseOper(ob, 0);
		if(prm == null)
			return null;
		if(top.v == "sinh")
			return new Sinh(prm);
		if(top.v == "cosh")
			return new Cosh(prm);
		if(top.v == "negate")
			return new Negate(prm);
	}

	if(top.t == 'O') {
		ob.i++;
		let prA = parseOper(ob, 1);
		let prB = parseOper(ob, 2);
		if (prA == null || prB == null) {
			error("Missed operand: ");
		}

	switch(top.v) {
		case "+":
			return new Add(prA, prB);
		case "-":
			return new Subtract(prA, prB);
		case "*":
			return new Multiply(prA, prB);
		case "/":
			return new Divide(prA, prB);	}
	}
	var i, e = ob.i +" :: " + ob.num + " >> ";
	for (i = 0; i < ob.num ; i++) {
		e = e + ob.m[i].t + " " + ob.m[i].v + " ; ";
	}
	error("Unrecognized expression " + e);
}

function parsePrefix(str) {

  var m = lexics(str);
  if (m == null)
		return null;	
  var ob = { m: m, i: 0, num: m.length };
  if (ob.num > 1 && (m[0].t == 'V' || m[0].t == 'N') ||
      ob.num < 4 && ob.m[0].t == 'L')	
		error("Wrong or missed operation inside parenthesis: " + str);

  var ret = parseOper(ob, 0);
  if(ret == null) {
	throw("Parsing error:" + str);
  }
  for (; ob.i < ob.num; ob.i++)
	if (ob.m[ob.i].t != 'R') {
		error("Extra operand found: " + s.substr(ob.m[ob.i].pos));
	}
  return ret;
}
