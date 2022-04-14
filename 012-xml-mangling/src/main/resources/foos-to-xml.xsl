<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
	xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:fo="http://www.w3.org/1999/XSL/Format" 
	xmlns:xs="http://www.w3.org/2001/XMLSchema" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	exclude-result-prefixes="fn fo xs"
>
	<xsl:output
		encoding="utf-8"
		indent="yes"
		method="xml"
		omit-xml-declaration="yes"
	/>

	<xsl:param name="FOOS1"/>
	<xsl:param name="FOOS2"/>

	<xsl:template match="/">
		<foos>
			<foos1>
				<xsl:apply-templates select="$FOOS1"/>
			</foos1>
			<foos2>
				<xsl:for-each select="$FOOS2">
					<foo2>
						<xsl:value-of select="."/>
					</foo2>
				</xsl:for-each>
			</foos2>
		</foos>
	</xsl:template>
	
	<xsl:template match="foo">
		<foo1>
			<xsl:value-of select="."/>
		</foo1>
	</xsl:template>
	
</xsl:stylesheet>
