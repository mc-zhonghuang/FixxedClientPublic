package net.ccbluex.liquidbounce.features.module.modules.player

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.AttackEvent
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.entity.Entity
import java.util.*

@ModuleInfo(name = "AutoL", category = ModuleCategory.PLAYER)
class AutoSay : Module() {
    val modeValue = ListValue("Mode", arrayOf("Chinese", "English", "Lvziqiao", "None"), "Lvziqiao")
    val QQorWebValue = ListValue("XC-Mode", arrayOf("Web","QQ"),"Web").displayable{modeValue.equals("Lvziqiao")}
    val Hytmode = BoolValue("HytMode",false)
    var R = Random()
    var abuse = arrayOf("[Fixxed-FREE] 失败者")
    var englishabuse = arrayOf("[Fixxed-FREE] |_")
    var Lvziqiao = arrayOf(
        "我是自闭吕子乔，初中辍学在家Sk1d出了Fixxed 小企鹅 Gr0up "+LiquidBounce.QQ_GROUP,
        "我是自闭吕子乔，我在线使用Fixxed Sk1d大端暴打你这个fvv  小企鹅 Gr0up "+LiquidBounce.QQ_GROUP,
        "我是吕子乔大蛇，就你这点三脚猫功夫还想暴打我，看我光速抽打你  小企鹅 Gr0up "+LiquidBounce.QQ_GROUP,
        "我是无敌的吕子乔，如果你要魔怔我的话，我就光速开你胡子，给你麻麻打电话  小企鹅 Gr0up "+LiquidBounce.QQ_GROUP,
        "我是你吕子乔大爹，我坚持努力了114514年，终于Sk1d出了Fixxed大Cl1ent  小企鹅 Gr0up "+LiquidBounce.QQ_GROUP
    )
    var Lvziqiao2 = arrayOf(
        "我是自闭吕子乔，初中辍学在家Sk1d出了Fixxed  Web: "+LiquidBounce.CLIENT_WEBSITE2,
        "我是自闭吕子乔，我在线使用Fixxed Sk1d大端暴打你这个fvv Web: "+LiquidBounce.CLIENT_WEBSITE2,
        "我是吕子乔大蛇，就你这点三脚猫功夫还想暴打我，看我光速抽打你  Web: "+LiquidBounce.CLIENT_WEBSITE2,
        "我是无敌的吕子乔，如果你要魔怔我的话，我就光速开你胡子，给你麻麻打电话  Web: "+LiquidBounce.CLIENT_WEBSITE2,
        "我是你吕子乔大爹，我坚持努力了114514年，终于Sk1d出了Fixxed大Cl1ent  Web :"+LiquidBounce.CLIENT_WEBSITE2
    )
    private var target: Entity? = null
    var kills = 0
    @EventTarget
    fun onAttack(event: AttackEvent) {
        target = event.targetEntity
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent?) {
        if (target != null) {
            if (target!!.isDead) {
                when (modeValue.get()) {
                    "Chinese" -> {
                        if(Hytmode.equals(true)){
                            mc.thePlayer.sendChatMessage(
                                "@"+"[" + LiquidBounce.CLIENT_NAME + "] L " + target!!.name + "  " + abuse[R.nextInt(
                                    abuse.size
                                )]
                            )
                        }else{
                            mc.thePlayer.sendChatMessage(
                                "[" + LiquidBounce.CLIENT_NAME + "] L " + target!!.name + "  " + abuse[R.nextInt(
                                    abuse.size
                                )]
                            )
                        }
                        kills += 1
                        target = null
                    }
                    "English" -> {
                        kills += 1
                        if(Hytmode.equals(true)){
                            mc.thePlayer.sendChatMessage(
                                "@"+"[" + LiquidBounce.CLIENT_NAME + "] L " + target!!.name + "  " + englishabuse[R.nextInt(
                                    englishabuse.size
                                )]
                            )
                        }else{
                            mc.thePlayer.sendChatMessage(
                                "[" + LiquidBounce.CLIENT_NAME + "] L " + target!!.name + "  " + englishabuse[R.nextInt(
                                    englishabuse.size
                                )]
                            )
                        }

                        target = null
                    }
                    "Lvziqiao" -> {
                        if(QQorWebValue.get()=="QQ"){
                            kills += 1
                            if(Hytmode.equals(true)){
                                mc.thePlayer.sendChatMessage(
                                    "@"+"[" + LiquidBounce.CLIENT_NAME + "] L " + target!!.name + "  " + Lvziqiao[R.nextInt(
                                        Lvziqiao.size
                                    )]
                                )
                            }else{
                                mc.thePlayer.sendChatMessage(
                                    "[" + LiquidBounce.CLIENT_NAME + "] L " + target!!.name + "  " + Lvziqiao[R.nextInt(
                                        Lvziqiao.size
                                    )]
                                )
                            }
                            target = null
                        }
                        else{
                            kills += 1
                            if(Hytmode.equals(true)){
                                mc.thePlayer.sendChatMessage(
                                    "@"+"[" + LiquidBounce.CLIENT_NAME + "] L " + target!!.name + "  " + Lvziqiao2[R.nextInt(
                                        Lvziqiao2.size
                                    )]
                                )
                            }else{
                                mc.thePlayer.sendChatMessage(
                                    "[" + LiquidBounce.CLIENT_NAME + "] L " + target!!.name + "  " + Lvziqiao2[R.nextInt(
                                        Lvziqiao2.size
                                    )]
                                )
                            }
                            target = null
                        }
                    }
                    "None" -> {
                        kills += 1
                        target = null
                    }
                }
            }
        }
    }

    override val tag: String
        get() = "Kills: $kills"
}