(function(){var a=Handlebars.template,b=Handlebars.templates=Handlebars.templates||{};b["plugin_configuration.hbs"]=a(function(a,b,c,d,e){function l(a,b){var d="",e,f;d+="\n<label>",f=c.description,f?e=f.call(a,{hash:{}}):(e=a.description,e=typeof e===h?e():e),d+=i(e),f=c.required,f?e=f.call(a,{hash:{},inverse:j.noop,fn:j.program(2,m,b)}):(e=a.required,e=typeof e===h?e():e),c.required||(e=k.call(a,e,{hash:{},inverse:j.noop,fn:j.program(2,m,b)}));if(e||e===0)d+=e;f=c.hidden,f?e=f.call(a,{hash:{},inverse:j.noop,fn:j.program(4,n,b)}):(e=a.hidden,e=typeof e===h?e():e),c.hidden||(e=k.call(a,e,{hash:{},inverse:j.noop,fn:j.program(4,n,b)}));if(e||e===0)d+=e;return d+='</label>\n<input type="text" value="',f=c.value,f?e=f.call(a,{hash:{}}):(e=a.value,e=typeof e===h?e():e),d+=i(e)+'" name="config.',f=c.key,f?e=f.call(a,{hash:{}}):(e=a.key,e=typeof e===h?e():e),d+=i(e)+'">\n',d}function m(a,b){return" (required)"}function n(a,b){return" (hidden)"}c=c||a.helpers;var f,g,h="function",i=this.escapeExpression,j=this,k=c.blockHelperMissing;return g=c.parameters,g?f=g.call(b,{hash:{},inverse:j.noop,fn:j.program(1,l,e)}):(f=b.parameters,f=typeof f===h?f():f),c.parameters||(f=k.call(b,f,{hash:{},inverse:j.noop,fn:j.program(1,l,e)})),f||f===0?f:""})})()