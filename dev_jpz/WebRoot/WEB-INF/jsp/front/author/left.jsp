<div id="sidebar" class="sidebar                  responsive">
				<script type="text/javascript">
					try{ace.settings.check('sidebar' , 'fixed')}catch(e){}
				</script>

				<div class="sidebar-shortcuts" id="sidebar-shortcuts">
					<div class="sidebar-shortcuts-large" id="sidebar-shortcuts-large">
						
						<button class="btn btn-info" title="切换菜单">
							<i class="ace-icon fa fa-pencil"></i>
						</button>
						
						<button class="btn btn-success" title="UI实例" onclick="window.open('static/html_UI/html');">
							<i class="ace-icon fa fa-signal"></i>
						</button>

						<!-- #section:basics/sidebar.layout.shortcuts -->
						<button class="btn btn-warning" title="" id="adminzidian">
							<i class="ace-icon fa fa-book"></i>
						</button>

						<button class="btn btn-danger">
							<i class="ace-icon fa fa-cogs"></i>
						</button>

						<!-- /section:basics/sidebar.layout.shortcuts -->
					</div>

					<div class="sidebar-shortcuts-mini" id="sidebar-shortcuts-mini">
						<span class="btn btn-success"></span>

						<span class="btn btn-info"></span>

						<span class="btn btn-warning"></span>

						<span class="btn btn-danger"></span>
					</div>
				</div><!-- /.sidebar-shortcuts -->

				<ul class="nav nav-list">
					<li class="open" id="lm1">
						<a style="cursor:pointer;" class="dropdown-toggle">
							<i class="menu-icon fa fa-desktop blue"></i>
							<span class="menu-text">
								软文
							</span>
							<b class="arrow fa fa-angle-down"></b>
						</a>
						<b class="arrow"></b>
						
						<ul class="submenu nav-show" style="display: block;">
						
							<li class="" id="z84">
								<a style="cursor:pointer;" onclick="AthorReload()">
									<i class="menu-icon fa fa-certificate purple"></i>
										软文列表
								</a>
								<b class="arrow"></b>
								
							</li>
						</ul>
					</li>
				

				</ul><!-- /.nav-list -->

				<!-- #section:basics/sidebar.layout.minimize -->
				<div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
					<i class="ace-icon fa fa-angle-double-left" data-icon1="ace-icon fa fa-angle-double-left" data-icon2="ace-icon fa fa-angle-double-right"></i>
				</div>

				<!-- /section:basics/sidebar.layout.minimize -->
				<script type="text/javascript">
					try{ace.settings.check('sidebar' , 'collapsed')}catch(e){}
				</script>
				<script type="text/javascript">
					function AthorReload(){
						window.parent.location.reload();
					}
				</script>
			</div>