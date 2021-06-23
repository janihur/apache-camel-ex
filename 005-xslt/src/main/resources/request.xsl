<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0"
	xmlns:s="http://example.com/service/v1"
	xmlns:shared="http://example.com/shared/v1"
	xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
	xmlns:cre="urn:microsoft-dynamics-schemas/codeunit/CreateOrder"
	xmlns:x50="urn:microsoft-dynamics-nav/xmlports/x50004"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	exclude-result-prefixes="s shared"
>
	<xsl:output encoding="utf-8" indent="yes" method="xml" omit-xml-declaration="yes"/>
	<xsl:strip-space elements="*"/>

	<xsl:param name="API-Client-Username"/>
	<xsl:param name="ENV"/>
	
	<xsl:variable name="isProduction" select="$ENV = 'PROD'"/>

    <xsl:template match="s:serviceRequest">
		<xsl:variable name="isDevelopmentOptions" select="boolean(s:developmentOptions)"/>
		<soapenv:Envelope>
			<soapenv:Header/>
			<soapenv:Body>
				<cre:CreateOrder>
					<cre:orderXML>
						<xsl:apply-templates select="shared:trackingHeader"/>
						<xsl:choose>
							<xsl:when test="$isDevelopmentOptions">
								<xsl:apply-templates select="s:developmentOptions"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:call-template name="createDevelopmentOptions"/>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:apply-templates select="s:order"/>
					</cre:orderXML>
					<cre:responseXML/>
				</cre:CreateOrder>
			</soapenv:Body>
		</soapenv:Envelope>
    </xsl:template>

	<!-- developmentOptions exists -->
	<xsl:template match="s:developmentOptions">
		<x50:developmentOptions>
			<xsl:choose>
				<!-- PRODUCTION -->
				<xsl:when test="$isProduction">
					<x50:orderingSystem>
						<xsl:value-of select="$API-Client-Username"/>
					</x50:orderingSystem>
				</xsl:when>
				<!-- NON-PRODUCTION -->
				<xsl:otherwise>
					<xsl:variable name="isOrderingSystem" select="boolean(s:orderingSystem)"/>
					<xsl:choose>
						<xsl:when test="$isOrderingSystem">
							<xsl:apply-templates select="s:orderingSystem"/>
						</xsl:when>
						<xsl:otherwise>
							<x50:orderingSystem>
								<xsl:value-of select="$API-Client-Username"/>
							</x50:orderingSystem>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:apply-templates select="s:environment"/>
				</xsl:otherwise>
			</xsl:choose>
		</x50:developmentOptions>
	</xsl:template>

	<!-- developmentOptions didn't exists -->
	<xsl:template name="createDevelopmentOptions">
		<x50:developmentOptions>
			<x50:orderingSystem>
				<xsl:value-of select="$API-Client-Username"/>
			</x50:orderingSystem>
		</x50:developmentOptions>
	</xsl:template>

	<!-- filtered -->
	<xsl:template match="shared:clientId"/>

	<!-- namespace conversion -->
	<xsl:template match="s:*|shared:*">
		<xsl:element name="x50:{local-name()}">
			<xsl:apply-templates select="node()|@*"/>
		</xsl:element>
	</xsl:template>

</xsl:stylesheet>
