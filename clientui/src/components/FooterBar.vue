<template>
<div class="footer">
	<div class="footer navbar-inverse navbar-fixed-bottom">
		<div class="center-block">
			<div class="small navbar-text text-center">
				<p>
					<a href="http://www.cancer.gov/global/web/policies" title="link to NCI Policies">Policies</a>
					&nbsp; | &nbsp;
					<a href="http://www.cancer.gov/global/web/policies/accessibility" title="link to NCI Accessibility Policies">Accessibility</a>
					&nbsp; | &nbsp;
					<a href="http://www.cancer.gov/global/viewing-files" title="link to NCI Tools for Viewing Files">Viewing Files</a>
					&nbsp; | &nbsp;
					<a href="http://www.cancer.gov/global/web/policies/foia" title="link to FOIA">FOIA</a>
				</p>
				<p>
					<a href="http://www.hhs.gov/" target="_blank" title="link to HHS.gov">U.S. Department of Health and Human Services</a>
					&nbsp; | &nbsp;
					<a href="http://www.nih.gov/" target="_blank" title="link to NIH.gov">National Institutes of Health</a>
					&nbsp; | &nbsp;
					<a href="http://www.cancer.gov/" title="link to Cancer.gov">National Cancer Institute</a>
					&nbsp; | &nbsp;
					<a href="http://www.usa.gov/" target="_blank" title="link to USA.gov">USA.gov</a>
				</p>
				<p>NIH… Turning Discovery Into Health ®</p>
			</div>
		</div>
	</div>
</div>
</template>

<script>
export default {
  name: 'FooterBar',
  props: {
    msg: String
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

.footer {
	position: fixed;
	bottom: 0;
	width: 100%;
	height: 60px;
	border:1px solid #1c5e86;
	color:#fff;
	background:#1c5e86;
	background:linear-gradient(#0188cb,#1c5e86);
	text-align: center;
}

p {
	margin: 0 auto;
	height: 15px;
	padding-top: 0px;
	padding-bottom: 0px;
}

a {
	font-size: 12px;
	color: #fff;
}

a:link { 
	color: #fff; 
	text-decoration: none
}

a:hover {
	color: #fff;
}

</style>
