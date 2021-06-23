<?xml version="1.0" encoding="UTF-8"?>
<?altova_samplexml msg1.xml?>
<xsl:stylesheet version="1.0" 
	xmlns:jh1="https://jani-hur.net/one"
	xmlns:jh2="https://jani-hur.net/two"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	exclude-result-prefixes="jh1"
>
	<xsl:output encoding="utf-8" indent="yes" method="xml" omit-xml-declaration="yes"/>
	<xsl:strip-space elements="*"/>

	<xsl:param name="EXTRA_LINE_1"/>
	<xsl:param name="EXTRA_LINE_2"/>

	<xsl:template match="jh1:message">
		<jh2:message>
			<xsl:apply-templates/>
			<xsl:if test="boolean($EXTRA_LINE_1)">
				<jh2:line>
					<xsl:value-of select="$EXTRA_LINE_1"/>
				</jh2:line>
			</xsl:if>
			<xsl:if test="boolean($EXTRA_LINE_2)">
				<jh2:line>
					<xsl:value-of select="$EXTRA_LINE_2"/>
				</jh2:line>
			</xsl:if>
		</jh2:message>
	</xsl:template>

	<!-- namespace conversion -->
	<xsl:template match="jh1:*">
		<xsl:element name="jh2:{local-name()}">
			<xsl:apply-templates select="node()|@*"/>
		</xsl:element>
	</xsl:template>

</xsl:stylesheet>
