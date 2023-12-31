package com.hotent.service.ws.cxf;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.xml.namespace.NamespaceContext;
import org.xml.sax.Locator;
import com.sun.xml.xsom.ForeignAttributes;
import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSComponent;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSIdentityConstraint;
import com.sun.xml.xsom.XSModelGroup;
import com.sun.xml.xsom.XSModelGroupDecl;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.XSType;
import com.sun.xml.xsom.XSWildcard;
import com.sun.xml.xsom.XmlString;
import com.sun.xml.xsom.parser.SchemaDocument;
import com.sun.xml.xsom.visitor.XSFunction;
import com.sun.xml.xsom.visitor.XSTermFunction;
import com.sun.xml.xsom.visitor.XSTermFunctionWithParam;
import com.sun.xml.xsom.visitor.XSTermVisitor;
import com.sun.xml.xsom.visitor.XSVisitor;

/**
 * 解决wsdl中有<s:element ref="s:schema" />元素时解析出错的问题
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月3日
 */
public class SchemaRefAny implements XSElementDecl{
	public SchemaRefAny(){}

	@Override
	public String getTargetNamespace() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean isAnonymous() {
		return false;
	}

	@Override
	public boolean isGlobal() {
		return false;
	}

	@Override
	public boolean isLocal() {
		return false;
	}

	@Override
	public XSAnnotation getAnnotation() {
		return null;
	}

	@Override
	public XSAnnotation getAnnotation(boolean createIfNotExist) {
		return null;
	}

	@Override
	public List<? extends ForeignAttributes> getForeignAttributes() {
		return null;
	}

	@Override
	public String getForeignAttribute(String nsUri, String localName) {
		return null;
	}

	@Override
	public Locator getLocator() {
		return null;
	}

	@Override
	public XSSchema getOwnerSchema() {
		return null;
	}

	@Override
	public XSSchemaSet getRoot() {
		return null;
	}

	@Override
	public SchemaDocument getSourceDocument() {
		return null;
	}

	@Override
	public Collection<XSComponent> select(String scd, NamespaceContext nsContext) {
		return null;
	}

	@Override
	public XSComponent selectSingle(String scd, NamespaceContext nsContext) {
		return null;
	}

	@Override
	public void visit(XSVisitor visitor) {
		
	}

	@Override
	public <T> T apply(XSFunction<T> function) {
		return null;
	}

	@Override
	public void visit(XSTermVisitor visitor) {
	}

	@Override
	public <T> T apply(XSTermFunction<T> function) {
		return null;
	}

	@Override
	public <T, P> T apply(XSTermFunctionWithParam<T, P> function, P param) {
		return null;
	}

	@Override
	public boolean isWildcard() {
		return false;
	}

	@Override
	public boolean isModelGroupDecl() {
		return false;
	}

	@Override
	public boolean isModelGroup() {
		return false;
	}

	@Override
	public boolean isElementDecl() {
		return false;
	}

	@Override
	public XSWildcard asWildcard() {
		return null;
	}

	@Override
	public XSModelGroupDecl asModelGroupDecl() {
		return null;
	}

	@Override
	public XSModelGroup asModelGroup() {
		return null;
	}

	@Override
	public XSElementDecl asElementDecl() {
		return null;
	}

	@Override
	public XSType getType() {
		return null;
	}

	@Override
	public boolean isNillable() {
		return false;
	}

	@Override
	public XSElementDecl getSubstAffiliation() {
		return null;
	}

	@Override
	public List<XSIdentityConstraint> getIdentityConstraints() {
		return null;
	}

	@Override
	public boolean isSubstitutionExcluded(int method) {
		return false;
	}

	@Override
	public boolean isSubstitutionDisallowed(int method) {
		return false;
	}

	@Override
	public boolean isAbstract() {
		return false;
	}

	@Override
	public XSElementDecl[] listSubstitutables() {
		return null;
	}

	@Override
	public Set<? extends XSElementDecl> getSubstitutables() {
		return null;
	}

	@Override
	public boolean canBeSubstitutedBy(XSElementDecl e) {
		return false;
	}

	@Override
	public XmlString getDefaultValue() {
		return null;
	}

	@Override
	public XmlString getFixedValue() {
		return null;
	}

	@Override
	public Boolean getForm() {
		return null;
	}
}